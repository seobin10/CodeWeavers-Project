package com.cw.cwu.service.admin;

import com.cw.cwu.domain.Course;
import com.cw.cwu.domain.CourseType;
import com.cw.cwu.domain.Department;
import com.cw.cwu.dto.CourseCreateRequestDTO;
import com.cw.cwu.dto.CourseInfoDTO;
import com.cw.cwu.dto.CourseUpdateRequestDTO;
import com.cw.cwu.repository.CourseRepository;
import com.cw.cwu.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class AdminCourseServiceImpl implements AdminCourseService {

    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public List<CourseInfoDTO> getCoursesByFilter(String departmentId) {
        List<Course> courses;

        if ("common".equalsIgnoreCase(departmentId)) {
            courses = courseRepository.findByDepartmentIsNull();
        } else {
            try {
                Integer deptId = Integer.parseInt(departmentId);
                courses = courseRepository.findByDepartment_DepartmentId(deptId);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("유효하지 않은 학과 ID입니다.");
            }
        }

        return courses.stream()
                .map(course -> new CourseInfoDTO(
                        course.getId(),
                        course.getName(),
                        course.getType(),
                        course.getCredit(),
                        course.getYear(),
                        course.getStatus().name(),
                        course.getDepartment() != null ? course.getDepartment().getDepartmentId() : null
                )).toList();
    }

    @Override
    public void createCourse(CourseCreateRequestDTO dto) {
        if (dto.getCourseName() == null || dto.getCourseName().isBlank()) {
            throw new IllegalArgumentException("과목명을 입력해주세요.");
        }

        if (courseRepository.existsByName(dto.getCourseName())) {
            throw new IllegalArgumentException("이미 존재하는 과목명입니다.");
        }

        Department dept = null;
        if (dto.getDepartmentId() != null) {
            dept = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학과입니다."));
        }

        if (dto.getCourseType() == CourseType.LIBERAL) {
            if (dto.getDepartmentId() != null) {
                throw new IllegalArgumentException("교양 과목은 학과에 소속될 수 없습니다.");
            }
        } else if (dto.getCourseType() == CourseType.MAJOR) {
            if (dto.getDepartmentId() == null) {
                throw new IllegalArgumentException("전공 과목은 반드시 학과가 지정되어야 합니다.");
            }
        }

        if (dto.getCourseType() == CourseType.MAJOR) {
            if (dto.getCredit() == null || dto.getCredit() != 3) {
                throw new IllegalArgumentException("전공 과목은 반드시 3학점이어야 합니다.");
            }
        } else if (dto.getCourseType() == CourseType.LIBERAL) {
            if (dto.getCredit() == null || dto.getCredit() < 1 || dto.getCredit() > 2) {
                throw new IllegalArgumentException("교양 과목은 1~2학점 사이로 설정해야 합니다.");
            }
        }

        if (dto.getCourseYear() == null || dto.getCourseYear() < 1 || dto.getCourseYear() > 4) {
            throw new IllegalArgumentException("학년은 1~4학년까지만 설정할 수 있습니다.");
        }

        Course course = Course.builder()
                .name(dto.getCourseName())
                .type(dto.getCourseType())
                .credit(dto.getCredit())
                .year(dto.getCourseYear())
                .department(dept)
                .status(dto.getStatus())
                .build();

        courseRepository.save(course);
    }

    @Override
    public void updateCourse(Integer id, CourseUpdateRequestDTO dto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("과목이 존재하지 않습니다."));

        if (dto.getNewName() != null && !dto.getNewName().isBlank()) {
            if (courseRepository.existsByName(dto.getNewName()) &&
                    !dto.getNewName().equals(course.getName())) {
                throw new IllegalArgumentException("이미 존재하는 과목명입니다.");
            }
            course.setName(dto.getNewName());
        }

        if (dto.getNewCredit() != null) {
            if (course.getType() == CourseType.MAJOR && dto.getNewCredit() != 3) {
                throw new IllegalArgumentException("전공 과목의 학점은 3학점으로만 설정할 수 있습니다.");
            }
            if (course.getType() == CourseType.LIBERAL &&
                    (dto.getNewCredit() < 1 || dto.getNewCredit() > 2)) {
                throw new IllegalArgumentException("교양 과목의 학점은 1~2학점 사이로만 변경할 수 있습니다.");
            }
            course.setCredit(dto.getNewCredit());
        }

        if (dto.getNewCourseYear() != null) {
            if (dto.getNewCourseYear() < 1 || dto.getNewCourseYear() > 4) {
                throw new IllegalArgumentException("학년은 1~4학년까지만 설정할 수 있습니다.");
            }
            course.setYear(dto.getNewCourseYear());
        }


        if (dto.getNewStatus() != null) {
            course.setStatus(dto.getNewStatus());
        }

        if (dto.getNewCredit() != null) {
            course.setCredit(dto.getNewCredit());
        }

        if (dto.getNewCourseYear() != null) {
            course.setYear(dto.getNewCourseYear());
        }

        courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Integer id) {
        if (!courseRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 과목이 존재하지 않습니다.");
        }

        try {
            courseRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("이 과목은 수업 이력이 있어 삭제할 수 없습니다.");
        }
    }



}