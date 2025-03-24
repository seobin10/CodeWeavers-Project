package com.cw.cwu.service.student;

import com.cw.cwu.dto.EnrollmentRequestDTO;
import com.cw.cwu.dto.PageRequestDTO;
import com.cw.cwu.dto.PageResponseDTO;

import java.util.List;
import java.util.Map;

public interface StudentEnrollmentService {
    PageResponseDTO<Map<String, Object>> getAvailableCoursesPaged(
            String studentId,
            String courseType,
            String departmentName,
            Integer courseYear,
            String classDay,
            Integer classStart,
            Integer credit,
            String courseName,
            PageRequestDTO pageRequestDTO
    );

    List<Map<String, Object>> getDepartments();

    List<Map<String, Object>> getCourseTypes();

    List<Map<String, Object>> getCourseYears();

    List<Map<String, Object>> getClassDays();

    List<Map<String, Object>> getClassTimes();

    List<Map<String, Object>> getCredits();

    public String applyToClass(EnrollmentRequestDTO requestDTO);
}