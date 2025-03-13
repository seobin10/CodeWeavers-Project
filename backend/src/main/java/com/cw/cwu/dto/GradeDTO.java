package com.cw.cwu.dto;

import com.cw.cwu.domain.StudentGrade;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradeDTO {
    private Long gradeId;
    private int enrollmentId;
    private String grade;
}
