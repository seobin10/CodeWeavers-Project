package com.cw.cwu.dto;

import com.cw.cwu.domain.AnswerChoice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationAnswerDTO {
    private Integer answerId;
    private Integer evaluationId;
    private Integer questionId;
    private String answerChoice;
    private String subjectiveText;

    public AnswerChoice changeEnum(String str) {
        if (str == null || str.isBlank()) return null;
        return switch (str) {
            case "5" -> AnswerChoice.VERY_GOOD;
            case "4" -> AnswerChoice.GOOD;
            case "3" -> AnswerChoice.AVERAGE;
            case "2" -> AnswerChoice.BAD;
            case "1" -> AnswerChoice.VERY_BAD;
            default -> throw new IllegalArgumentException("Invalid answer choice: " + str);
        };
    }

    public String changeString(AnswerChoice choice) {
        return switch (choice) {
            case VERY_GOOD -> "5";
            case GOOD -> "4";
            case AVERAGE -> "3";
            case BAD -> "2";
            case VERY_BAD -> "1";
            default -> "";
        };
    }

}