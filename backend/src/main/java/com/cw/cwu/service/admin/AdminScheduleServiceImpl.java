package com.cw.cwu.service.admin;

import com.cw.cwu.domain.ScheduleSetting;
import com.cw.cwu.domain.ScheduleType;
import com.cw.cwu.domain.Semester;
import com.cw.cwu.domain.SemesterTerm;
import com.cw.cwu.dto.ScheduleRequestDTO;
import com.cw.cwu.dto.ScheduleResponseDTO;
import com.cw.cwu.dto.SemesterRequestDTO;
import com.cw.cwu.dto.SemesterResponseDTO;
import com.cw.cwu.repository.ScheduleSettingRepository;
import com.cw.cwu.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class AdminScheduleServiceImpl implements AdminScheduleService {

    private final ScheduleSettingRepository scheduleSettingRepository;
    private final SemesterRepository semesterRepository;

    @Override
    public void createSemester(SemesterRequestDTO dto) {
        boolean exists = semesterRepository.existsByYearAndTerm(dto.getYear(), dto.getTerm());
        if (exists) {
            throw new IllegalArgumentException("이미 동일한 연도/학기의 학기가 존재합니다.");
        }

        Semester semester = Semester.builder()
                .year(dto.getYear())
                .term(dto.getTerm())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();

        semesterRepository.save(semester);
    }

    @Override
    public List<SemesterResponseDTO> getAllSemesters() {
        return semesterRepository.findAll().stream()
                .sorted((s1, s2) -> {
                    int yearCompare = s2.getYear() - s1.getYear();
                    if (yearCompare != 0) return yearCompare;

                    if (s1.getTerm() == SemesterTerm.FIRST && s2.getTerm() == SemesterTerm.SECOND) return -1;
                    if (s1.getTerm() == SemesterTerm.SECOND && s2.getTerm() == SemesterTerm.FIRST) return 1;
                    return 0;
                })
                .map(s -> {
                    boolean isLinked = scheduleSettingRepository.existsBySemesterId(s.getId());
                    return new SemesterResponseDTO(
                            s.getId(),
                            s.getYear(),
                            s.getTerm(),
                            s.getStartDate(),
                            s.getEndDate()
                    );
                })
                .toList();
    }



    @Override
    public void updateSemester(Integer semesterId, SemesterRequestDTO dto) {
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new IllegalArgumentException("해당 학기를 찾을 수 없습니다."));

        boolean isLinkedToSchedule = scheduleSettingRepository.existsBySemesterId(semesterId);

        if (isLinkedToSchedule) {
            if (!semester.getYear().equals(dto.getYear()) || !semester.getTerm().equals(dto.getTerm())) {
                throw new IllegalStateException("해당 학기는 이미 학사 일정에 사용되고 있어 연도 또는 학기 정보는 수정할 수 없습니다.");
            }
        } else {
            semester.setYear(dto.getYear());
            semester.setTerm(dto.getTerm());
        }

        semester.setStartDate(dto.getStartDate());
        semester.setEndDate(dto.getEndDate());

        semesterRepository.save(semester);
    }


    @Override
    public void deleteSemester(Integer semesterId) {
        if (scheduleSettingRepository.existsBySemesterId(semesterId)) {
            throw new IllegalStateException("해당 학기에 연결된 일정이 있어 삭제할 수 없습니다.");
        }
        semesterRepository.deleteById(semesterId);
    }

    /**
     * 학사 일정 등록 또는 수정
     */
    @Override
    public String setSchedule(ScheduleRequestDTO dto) {
        Semester semester = semesterRepository.findById(dto.getSemesterId())
                .orElseThrow(() -> new IllegalArgumentException("해당 학기를 찾을 수 없습니다."));

        ScheduleSetting setting = scheduleSettingRepository
                .findBySemesterIdAndScheduleType(dto.getSemesterId(), dto.getScheduleType())
                .orElse(ScheduleSetting.builder()
                        .scheduleType(dto.getScheduleType())
                        .semester(semester)
                        .build());

        setting.setStartDate(dto.getStartDate());
        setting.setEndDate(dto.getEndDate());
        setting.setDescription(dto.getDescription());

        scheduleSettingRepository.save(setting);
        return "학사 일정이 저장되었습니다.";
    }


    /**
     * 특정 학사 일정 조회
     */
    @Override
    public ScheduleResponseDTO getSchedule(ScheduleType type, Integer semesterId) {
        ScheduleSetting setting = scheduleSettingRepository
                .findBySemesterIdAndScheduleType(semesterId, type)
                .orElse(null);

        if (setting == null) {
            return null;
        }

        return new ScheduleResponseDTO(
                setting.getSemester().getId(),
                setting.getScheduleType(),
                setting.getStartDate(),
                setting.getEndDate(),
                setting.getDescription()
        );
    }

    /**
     * 현재 날짜가 기간 내인지 여부 확인
     */
    @Override
    public boolean isScheduleOpen(ScheduleType scheduleType) {
        LocalDateTime now = LocalDateTime.now();

        ScheduleSetting setting = scheduleSettingRepository
                .findByScheduleTypeAndStartDateBeforeAndEndDateAfter(scheduleType, now, now)
                .orElse(null);

        return setting != null; // 설정이 존재하면 열린 것
    }

    @Override
    public Integer getCurrentSemesterId() {
        LocalDate today = LocalDate.now();

        Semester currentSemester = semesterRepository
                .findByStartDateBeforeAndEndDateAfter(today, today)
                .orElseThrow(() -> new IllegalStateException("현재 학기가 존재하지 않습니다."));

        return currentSemester.getId();
    }

    // 현재 날짜가 포함된 특정 일정의 학기에 해당하는 semesterId 반환
    @Override
    public Integer getSemesterIdByScheduleType(ScheduleType type) {
        LocalDateTime now = LocalDateTime.now();

        ScheduleSetting setting = scheduleSettingRepository
                .findByScheduleTypeAndStartDateBeforeAndEndDateAfter(type, now, now)
                .orElseThrow(() -> new IllegalStateException("현재 " + type.name() + " 일정이 아닙니다."));

        return setting.getSemester().getId();
    }

    // 수강신청 학기 찾기
    @Override
    public Integer getEnrollSemesterId() {
        return getSemesterIdByScheduleType(ScheduleType.ENROLL);
    }


    // 다음 학기(오늘 이후 시작) ID 조회
    @Override
    public Optional<Integer> getUpcomingSemesterId() {
        LocalDate today = LocalDate.now();
        return semesterRepository.findFirstByStartDateAfterOrderByStartDateAsc(today)
                .map(Semester::getId);
    }

}