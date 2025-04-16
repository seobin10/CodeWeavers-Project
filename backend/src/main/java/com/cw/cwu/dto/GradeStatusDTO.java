package com.cw.cwu.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeStatusDTO {
    private String studentId;
    private String studentName;
    private String departmentName;

    private String status; // "미입력 있음", "GPA 불일치", "이상 없음"

    private Float recordedGpa;     // 저장된 GPA
    private Float calculatedGpa;   // 실시간 계산 GPA
    private int missingGradesCount;
}
