package com.cw.cwu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationQuestionDTO {
    private Integer question_id;
    private String questionText;
    private Boolean isSubjective;
}
