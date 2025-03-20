package com.cw.cwu.dto;

import com.cw.cwu.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {
    private Integer questionId;
    private String title;
    private String userName;
    private LocalDate createdAt;
    private String status;
    private int viewCount;

    // ENUM TO STRING
    public String statusToString(Status enumData) {
        return switch (enumData) {
            case OPEN -> "미답변";
            case ANSWERED -> "답변완료";
            default -> enumData.name();
        };
    }

    // STRING TO ENUM
    public Status statusToEnum(String str) {
        return switch (str) {
            case "미답변" -> Status.OPEN;
            case "답변완료" -> Status.ANSWERED;
            default -> Status.valueOf(str);
        };
    }
}
