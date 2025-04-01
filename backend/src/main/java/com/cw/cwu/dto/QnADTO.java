package com.cw.cwu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class QnADTO {
    private int questionId;
    private int viewCount;
    private String title;
    private String userName;
    private LocalDate createdAt;
    private String questionContent;
    private String answerContent;
}
