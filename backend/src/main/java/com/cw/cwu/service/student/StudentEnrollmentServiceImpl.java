package com.cw.cwu.service.student;

import com.cw.cwu.domain.Enrollment;
import com.cw.cwu.dto.EnrollmentRequestDTO;
import com.cw.cwu.repository.EnrollmentRepository;
import com.cw.cwu.repository.student.FilterRepository;
import com.cw.cwu.repository.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentEnrollmentServiceImpl implements StudentEnrollmentService {

    private final StudentRepository studentRepository;
    private final FilterRepository filterRepository;
    private final ModelMapper modelMapper;
    private final EnrollmentRepository enrollmentRepository;

    // 학생이 수강 신청 가능한 강의 목록 조회
    public List<Map<String, Object>> getAvailableCourses(
            String studentId,
            String courseType,
            String departmentName,
            Integer courseYear,
            String classDay,
            Integer classStart,
            Integer credit,
            String courseName) {
        return studentRepository.findAvailableCourses(studentId, courseType, departmentName, courseYear, classDay, classStart, credit, courseName);
    }

    // 학과 목록 필터링
    public List<Map<String, Object>> getDepartments() {
        return filterRepository.findDistinctDepartments().stream()
                .map(d -> Map.<String, Object>of("departmentName", d)) // 여기서 Object로 캐스팅
                .collect(Collectors.toList());
    }

    // 강의 구분 필터링
    public List<Map<String, Object>> getCourseTypes() {
        return filterRepository.findDistinctCourseTypes().stream()
                .map(ct -> Map.<String, Object>of("courseType",
                        ct.equals("MAJOR") ? "전공" : "교양"))
                .collect(Collectors.toList());
    }

    // 강의 학년 필터링
    public List<Map<String, Object>> getCourseYears() {
        return filterRepository.findDistinctCourseYears().stream()
                .map(y -> Map.<String, Object>of("courseYear", y))
                .collect(Collectors.toList());
    }

    // 강의 요일 필터링
    public List<Map<String, Object>> getClassDays() {
        return filterRepository.findDistinctClassDays().stream()
                .map(cd -> Map.<String, Object>of("classDay", cd))
                .collect(Collectors.toList());
    }

    // 강의 시작 시간 필터링
    public List<Map<String, Object>> getClassTimes() {
        return filterRepository.findDistinctClassTimes().stream()
                .map(ct -> Map.<String, Object>of("classTime", ct))
                .collect(Collectors.toList());
    }

    // 학점 목록 조회 필터링
    public List<Map<String, Object>> getCredits() {
        return filterRepository.findDistinctCredits().stream()
                .map(c -> Map.<String, Object>of("credit", c))
                .collect(Collectors.toList());
    }


    // 수강 신청 처리
    public void applyToClass(EnrollmentRequestDTO requestDTO) {
        Enrollment enrollment = modelMapper.map(requestDTO, Enrollment.class);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollmentRepository.save(enrollment);
    }


}
