package com.cw.cwu.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer answerId;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question questionId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(name = "content")
    private String answer;

    @Column(name = "created_at")
    private LocalDate AnswerDate;
}
