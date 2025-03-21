package com.cw.cwu.service.student;

public interface StudentInfoService {
    int calculateStudentYear(String studentId);
    boolean checkGraduationEligibility(String studentId);
}