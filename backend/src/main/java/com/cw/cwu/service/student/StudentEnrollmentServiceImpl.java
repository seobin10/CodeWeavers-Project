package com.cw.cwu.service.student;

import com.cw.cwu.repository.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentEnrollmentServiceImpl implements StudentEnrollmentService {

    private final StudentRepository studentRepository;

    // 학생이 수강 신청 가능한 강의 목록 조회
    public List<Map<String, Object>> getAvailableCourses(
            String studentId,
            String courseType,
            Integer departmentId,
            Integer courseYear,
            String classDay,
            Integer classStart,
            Integer credit,
            String courseName) {
        return studentRepository.findAvailableCourses(studentId, courseType, departmentId, courseYear, classDay, classStart, credit, courseName);
    }
}
