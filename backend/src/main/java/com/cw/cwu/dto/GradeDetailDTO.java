package com.cw.cwu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GradeDetailDTO {
    private String studentId;
    private String studentName;
    private String courseName;
    private Integer credit;
    private String grade;

    private Integer enrollmentId;
    private Integer gradeId;
    private Boolean isCurrentSemester;
}
