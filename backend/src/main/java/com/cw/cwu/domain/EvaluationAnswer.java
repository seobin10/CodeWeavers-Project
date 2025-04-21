package com.cw.cwu.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "evaluation_answers")
public class EvaluationAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Integer answerId;

    @ManyToOne
    @JoinColumn(name = "evaluation_id", nullable = false)
    private EvaluationLecture evaluationId;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private EvaluationQuestion questionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "answer_choice", nullable = true)
    private AnswerChoice answerChoice;

    @Column(name = "subjective_text")
    private String subjectiveText;
}
