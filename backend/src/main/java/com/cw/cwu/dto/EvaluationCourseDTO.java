package com.cw.cwu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationCourseDTO {
    private Integer classId;        // 수강한 강의 ID
    private String courseName;     // 과목명
    private String professorName;  // 교수명
    private int credit;            // 학점
}
