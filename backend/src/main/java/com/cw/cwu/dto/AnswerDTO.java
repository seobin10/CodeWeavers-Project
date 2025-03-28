package com.cw.cwu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AnswerDTO {
    private Integer answerId;
    private Integer questionId;
    private String userId;
    private String answer;
    private LocalDate answerDate;
}
