package com.cw.cwu.service.professor;

import com.cw.cwu.domain.*;
import com.cw.cwu.dto.*;
import com.cw.cwu.repository.*;
import com.cw.cwu.util.AuthUtil;
import com.cw.cwu.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessorClassServiceImpl implements ProfessorClassService {

    private final ClassEntityRepository classEntityRepository;
    private final CourseRepository courseRepository;
    private final LectureRoomRepository lectureRoomRepository;
    private final UserRepository userRepository;
    private final SemesterRepository semesterRepository;

    // 시간 중복 체크
    private boolean isTimeOverlap(int start1, int end1, int start2, int end2) {
        return start1 <= end2 && start2 <= end1;
    }


    private String validateClassConstraints(String professorId, Semester semester, String day,
                                            Integer startTime, Integer endTime, Integer capacity,
                                            Integer classIdToExclude, Integer lectureRoomId) {
        if (semester == null) return "유효한 학기 정보가 없습니다.";

        if (startTime == null || endTime == null || startTime < 1 || endTime > 10 || startTime > endTime) {
            return "시간 설정이 올바르지 않습니다.";
        }

        if (capacity < 20 || capacity > 50) {
            return "수강 정원은 20명 이상, 50명 이하로 설정해야 합니다.";
        }

        List<ClassEntity> professorClasses = classEntityRepository
                .findByProfessor_UserIdAndDayAndSemester(professorId, day, semester);

        for (ClassEntity cls : professorClasses) {
            if (!cls.getId().equals(classIdToExclude) &&
                    isTimeOverlap(cls.getStartTime(), cls.getEndTime(), startTime, endTime)) {
                return "해당 시간에 이미 등록된 강의가 있습니다.";
            }
        }

        if (lectureRoomId != null) {
            List<ClassEntity> roomClasses = classEntityRepository
                    .findByLectureRoom_IdAndDayAndSemester(lectureRoomId, day, semester);

            for (ClassEntity cls : roomClasses) {
                if (!cls.getId().equals(classIdToExclude) &&
                        isTimeOverlap(cls.getStartTime(), cls.getEndTime(), startTime, endTime)) {
                    return "해당 강의실은 이미 사용 중입니다.";
                }
            }
        }

        return null;
    }

    private Semester getCurrentSemester() {
        return semesterRepository.findCurrentSemester(LocalDate.now())
                .orElseThrow(() -> new IllegalArgumentException("현재 학기를 찾을 수 없습니다."));
    }

    @Transactional
    @Override
    public String createClass(ClassCreateRequestDTO dto) {
        // 기본 검증
        if (dto.getCourseId() == null || !courseRepository.existsById(dto.getCourseId())) {
            return "존재하지 않는 과목입니다.";
        }

        if (dto.getLectureRoomId() == null || !lectureRoomRepository.existsById(dto.getLectureRoomId())) {
            return "존재하지 않는 강의실입니다.";
        }

        User professor = userRepository.findById(dto.getProfessorId()).orElse(null);
        if (professor == null) {
            return "해당 교수를 찾을 수 없습니다.";
        }

        Semester semester = getCurrentSemester();

        // 시간 및 강의실 중복 검증
        String validationError = validateClassConstraints(
                dto.getProfessorId(), semester, dto.getDay(),
                dto.getStartTime(), dto.getEndTime(), dto.getCapacity(),
                null, dto.getLectureRoomId()
        );
        if (validationError != null) return validationError;

        LectureRoom lectureRoom = lectureRoomRepository.findById(dto.getLectureRoomId()).get();
        Course course = courseRepository.findById(dto.getCourseId()).get();

        ClassEntity classEntity = ClassEntity.builder()
                .course(course)
                .professor(professor)
                .semester(semester)
                .day(dto.getDay())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .capacity(dto.getCapacity())
                .enrolled(0)
                .lectureRoom(lectureRoom)
                .build();

        classEntityRepository.save(classEntity);
        return "성공";
    }

    @Override
    public PageResponseDTO<ClassDTO> getMyClasses(String professorId, PageRequestDTO pageRequestDTO, Integer semesterId) {
        Pageable pageable = PageUtil.toPageable(pageRequestDTO, "id");

        Page<ClassEntity> page;

        if (semesterId != null) {
            page = classEntityRepository.findByProfessor_UserIdAndSemester_Id(professorId, semesterId, pageable);
        } else {
            page = classEntityRepository.findByProfessor_UserId(professorId, pageable);
        }

        Page<ClassDTO> dtoPage = page.map(entity -> {
            Semester semester = entity.getSemester();
            ClassDTO dto = new ClassDTO();
            dto.setClassId(entity.getId());
            dto.setCourseName(entity.getCourse().getName());
            dto.setSemester(
                    semester.getYear() + "-" + (semester.getTerm() == SemesterTerm.FIRST ? "1" : "2")
            );
            dto.setDay(entity.getDay());
            dto.setStartTime(entity.getStartTime());
            dto.setEndTime(entity.getEndTime());
            dto.setCapacity(entity.getCapacity());
            dto.setEnrolled(entity.getEnrolled());
            dto.setLectureRoomName(entity.getLectureRoom().getName());
            dto.setBuildingName(entity.getLectureRoom().getBuilding().getName());
            dto.setCourseType(entity.getCourse().getType().name());
            dto.setCourseYear(entity.getCourse().getYear());

            return dto;
        });

        return PageUtil.toDTO(dtoPage, pageRequestDTO.getPage());
    }

    @Transactional
    @Override
    public String updateClass(ClassUpdateRequestDTO dto, String professorId) throws AccessDeniedException {
        ClassEntity entity = classEntityRepository.findById(dto.getClassId()).orElse(null);
        if (entity == null) return "해당 강의를 찾을 수 없습니다.";

        // 교수 권한 체크
        AuthUtil.checkOwnership(entity.getProfessor().getUserId(), professorId);

        Semester semester = getCurrentSemester();

        // 검증
        String validationError = validateClassConstraints(
                entity.getProfessor().getUserId(), semester, dto.getDay(),
                dto.getStartTime(), dto.getEndTime(), dto.getCapacity(),
                entity.getId(), dto.getLectureRoomId()
        );
        if (validationError != null) return validationError;

        entity.setDay(dto.getDay());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setCapacity(dto.getCapacity());

        if (dto.getLectureRoomId() != null) {
            LectureRoom room = lectureRoomRepository.findById(dto.getLectureRoomId()).orElse(null);
            entity.setLectureRoom(room);
        }

        // 학기 정보 업데이트
        entity.setSemester(semester);

        classEntityRepository.save(entity);
        return "수정 완료";
    }

    @Transactional
    @Override
    public String deleteClass(Integer classId, String professorId) throws AccessDeniedException {
        ClassEntity entity = classEntityRepository.findById(classId).orElse(null);
        if (entity == null) return "해당 강의를 찾을 수 없습니다.";


        AuthUtil.checkOwnership(entity.getProfessor().getUserId(), professorId);

        classEntityRepository.delete(entity);
        return "삭제 완료";
    }

    @Override
    public List<CourseSimpleDTO> getCoursesByProfessor(String professorId) {
        User professor = userRepository.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("교수 정보가 없습니다."));

        Integer deptId = professor.getDepartment().getDepartmentId();

        List<Course> courses = courseRepository.findCoursesByDepartmentOrLiberal(deptId, CourseType.LIBERAL);

        return courses.stream()
                .map(c -> new CourseSimpleDTO(c.getId(), c.getName(), c.getType(), c.getYear()))
                .collect(Collectors.toList());
    }

    @Override
    public List<LectureRoomSimpleDTO> getAvailableLectureRooms(String day, int startTime, int endTime) {
        Semester semester = getCurrentSemester();

        List<LectureRoom> allRooms = lectureRoomRepository.findAll();

        return allRooms.stream()
                .filter(room -> {
                    List<ClassEntity> classes = classEntityRepository.findByLectureRoom_IdAndDayAndSemester(
                            room.getId(), day, semester
                    );
                    return classes.stream().noneMatch(c ->
                            startTime <= c.getEndTime() && c.getStartTime() <= endTime
                    );
                })
                .map(r -> new LectureRoomSimpleDTO(
                        r.getId(),
                        r.getName(),
                        r.getBuilding().getName()
                ))
                .collect(Collectors.toList());
    }
}