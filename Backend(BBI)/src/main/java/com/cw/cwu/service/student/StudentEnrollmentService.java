package com.cw.cwu.service.student;

import com.cw.cwu.dto.EnrollmentRequestDTO;

import java.util.List;
import java.util.Map;

public interface StudentEnrollmentService {
    List<Map<String, Object>> getAvailableCourses(
            String studentId, String courseType, String department_name,
            Integer courseYear, String classDay, Integer classStart,
            Integer credit, String courseName);

    List<Map<String, Object>> getDepartments();

    List<Map<String, Object>> getCourseTypes();

    List<Map<String, Object>> getCourseYears();

    List<Map<String, Object>> getClassDays();

    List<Map<String, Object>> getClassTimes();

    List<Map<String, Object>> getCredits();

    void applyToClass(EnrollmentRequestDTO requestDTO);
}