package com.cw.cwu.service.admin;

import com.cw.cwu.domain.ScheduleSetting;
import com.cw.cwu.domain.ScheduleType;
import com.cw.cwu.domain.Semester;
import com.cw.cwu.dto.ScheduleRequestDTO;
import com.cw.cwu.dto.ScheduleResponseDTO;
import com.cw.cwu.repository.ScheduleSettingRepository;
import com.cw.cwu.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AdminScheduleServiceImpl implements AdminScheduleService {

    private final ScheduleSettingRepository scheduleSettingRepository;
    private final SemesterRepository semesterRepository;  // Semester Repository 추가

    /**
     * 학사 일정 등록 또는 수정
     */
    @Override
    public String setSchedule(ScheduleRequestDTO dto) {
        // 해당 학기 정보 조회
        Semester semester = semesterRepository.findById(dto.getSemesterId())
                .orElseThrow(() -> new IllegalArgumentException("해당 학기를 찾을 수 없습니다."));

        // 학사 일정 존재 여부 확인 후 저장
        ScheduleSetting setting = scheduleSettingRepository.findByScheduleType(dto.getScheduleType())
                .orElse(ScheduleSetting.builder()
                        .scheduleType(dto.getScheduleType())
                        .semester(semester)  // 학기 정보 설정
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
    public ScheduleResponseDTO getSchedule(ScheduleType type) {
        ScheduleSetting setting = scheduleSettingRepository.findByScheduleType(type)
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
    public boolean isScheduleOpen(ScheduleType type) {
        ScheduleSetting setting = scheduleSettingRepository.findByScheduleType(type)
                .orElse(null);

        if (setting == null) return false;

        LocalDate today = LocalDate.now();
        return (today.isEqual(setting.getStartDate()) || today.isAfter(setting.getStartDate()))
                && (today.isEqual(setting.getEndDate()) || today.isBefore(setting.getEndDate()));
    }
}