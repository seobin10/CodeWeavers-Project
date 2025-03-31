package com.cw.cwu.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EnrollmentResponseDTO {
    private int enrollmentId;
    private String studentId;
    private String studentName;
    private int classId;
    private String courseName;
    private LocalDate enrollmentDate;
}