package com.cw.cwu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationLectureDTO {
    private Integer evaluationId;
    private String studentId;
    private Integer classId;
    private Timestamp createdAt;
}
