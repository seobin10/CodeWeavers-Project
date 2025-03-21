package com.cw.cwu.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentRequestDTO {
    private String studentId; // 신청 학생 ID
    private int classId; // 신청할 강의 ID
}
