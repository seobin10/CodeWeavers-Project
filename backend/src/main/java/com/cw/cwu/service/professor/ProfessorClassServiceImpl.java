package com.cw.cwu.service.professor;

import com.cw.cwu.domain.ClassEntity;
import com.cw.cwu.domain.Course;
import com.cw.cwu.domain.LectureRoom;
import com.cw.cwu.domain.User;
import com.cw.cwu.dto.*;
import com.cw.cwu.repository.professor.CourseRepository;
import com.cw.cwu.repository.professor.LectureRoomRepository;
import com.cw.cwu.repository.student.ClassRepository;
import com.cw.cwu.repository.user.UserRepository;
import com.cw.cwu.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessorClassServiceImpl implements ProfessorClassService {

    private final ClassRepository classRepository;
    private final CourseRepository courseRepository;
    private final LectureRoomRepository lectureRoomRepository;
    private final UserRepository userRepository;

    @Override
    public String createClass(ClassCreateRequestDTO dto) {
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

        Course course = courseRepository.findById(dto.getCourseId()).get();
        LectureRoom lectureRoom = lectureRoomRepository.findById(dto.getLectureRoomId()).get();

        ClassEntity classEntity = ClassEntity.builder()
                .course(course)
                .professor(professor)
                .semester(dto.getSemester())
                .day(dto.getDay())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .capacity(dto.getCapacity())
                .enrolled(0)
                .lectureRoom(lectureRoom)
                .build();

        classRepository.save(classEntity);
        return "성공";
    }

    @Override
    public PageResponseDTO<ClassDTO> getMyClasses(String professorId, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageUtil.toPageable(pageRequestDTO, "id");

        Page<ClassEntity> page = classRepository.findByProfessor_UserId(professorId, pageable);

        Page<ClassDTO> dtoPage = page.map(entity -> {
            ClassDTO dto = new ClassDTO();
            dto.setClassId(entity.getId());
            dto.setCourseName(entity.getCourse().getName());
            dto.setSemester(entity.getSemester());
            dto.setDay(entity.getDay());
            dto.setStartTime(entity.getStartTime());
            dto.setEndTime(entity.getEndTime());
            dto.setCapacity(entity.getCapacity());
            dto.setEnrolled(entity.getEnrolled());
            dto.setLectureRoomName(entity.getLectureRoom().getName());
            dto.setBuildingName(entity.getLectureRoom().getBuilding().getName());
            return dto;
        });

        return PageUtil.toDTO(dtoPage, pageRequestDTO.getPage());
    }

    @Override
    public String updateClass(ClassUpdateRequestDTO dto) {
        ClassEntity entity = classRepository.findById(dto.getClassId()).orElse(null);
        if (entity == null) return "해당 강의를 찾을 수 없습니다.";

        entity.setDay(dto.getDay());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setCapacity(dto.getCapacity());

        if (dto.getLectureRoomId() != null) {
            LectureRoom room = lectureRoomRepository.findById(dto.getLectureRoomId()).orElse(null);
            entity.setLectureRoom(room);
        }

        classRepository.save(entity);
        return "수정 완료";
    }

    @Override
    public String deleteClass(Integer classId) {
        if (!classRepository.existsById(classId)) {
            return "해당 강의를 찾을 수 없습니다.";
        }

        classRepository.deleteById(classId);
        return "삭제 완료";
    }

    @Override
    public List<CourseSimpleDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(c -> new CourseSimpleDTO(c.getId(), c.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<LectureRoomSimpleDTO> getAllLectureRooms() {
        List<LectureRoom> rooms = lectureRoomRepository.findAll();
        return rooms.stream()
                .map(r -> new LectureRoomSimpleDTO(
                        r.getId(),
                        r.getName(),
                        r.getBuilding().getName()
                ))
                .collect(Collectors.toList());
    }
}
