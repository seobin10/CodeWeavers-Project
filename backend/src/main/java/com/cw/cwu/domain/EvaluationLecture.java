package com.cw.cwu.domain;

import io.jsonwebtoken.lang.Classes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "lecture_evaluations")
public class EvaluationLecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id")
    private Integer evaluationId;

    @ManyToOne
    @JoinColumn(name = "student_id", unique = true, nullable = false)
    private User studentId;

    @ManyToOne
    @JoinColumn(name = "classId", unique = true, nullable = false)
    private ClassEntity classId;

    @Column(name = "created_at")
    private Timestamp createdAt;
}

