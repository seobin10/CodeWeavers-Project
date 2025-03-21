package com.cw.cwu.service.student;

import java.util.List;
import java.util.Map;

public interface StudentEnrollmentService {
    List<Map<String, Object>> getAvailableCourses(
            String studentId, String courseType, Integer departmentId,
            Integer courseYear, String classDay, Integer classStart,
            Integer credit, String courseName);
}