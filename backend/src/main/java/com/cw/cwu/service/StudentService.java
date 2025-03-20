package com.cw.cwu.service;

import com.cw.cwu.dto.GradeDTO;
import java.util.List;
import java.util.Map;

public interface StudentService {
    public void updateStudentRecords(String studentId);
    public int calculateStudentYear(String studentId);
    public boolean checkGraduationEligibility(String studentId);
    public List<GradeDTO> getStudentGrade(String studentId);
    public List<Map<String, Object>> getAvailableCourses(String studentId, String courseType, Integer departmentId, Integer courseYear, String classDay, Integer classStart, Integer credit, String courseName);
}