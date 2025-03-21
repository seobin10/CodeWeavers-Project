package com.cw.cwu.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EnrollmentResponseDTO {
    private int enrollmentId; // 수강 신청 ID (DB에서 자동 생성)
    private String studentId; // 신청 학생 ID
    private String studentName; // 신청 학생 이름
    private int classId; // 강의 ID
    private String courseName; // 강의명
    private LocalDate enrollmentDate; // 신청 날짜
}