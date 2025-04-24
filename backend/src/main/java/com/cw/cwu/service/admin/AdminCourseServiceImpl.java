package com.cw.cwu.service.admin;

import com.cw.cwu.domain.Course;
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
                        course.getStatus().name()
                )).toList();
    }

    @Override
    public void createCourse(CourseCreateRequestDTO dto) {
        if (courseRepository.existsByName(dto.getCourseName())) {
            throw new IllegalArgumentException("이미 존재하는 과목명입니다.");
        }

        Department dept = null;
        if (dto.getDepartmentId() != null) {
            dept = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학과입니다."));
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