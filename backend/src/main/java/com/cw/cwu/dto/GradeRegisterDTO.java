package com.cw.cwu.dto;

import com.cw.cwu.domain.StudentGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GradeRegisterDTO {
    private Integer professorId;
    private Integer enrollmentId;
    private StudentGrade grade;
}
