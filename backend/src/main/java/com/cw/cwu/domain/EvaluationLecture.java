package com.cw.cwu.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lecture_evaluations", uniqueConstraints = @UniqueConstraint(name = "uc_student_class", columnNames = {"student_id", "classId"}))
public class EvaluationLecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id")
    private Integer evaluationId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User studentId;

    @ManyToOne
    @JoinColumn(name = "classId", nullable = false)
    private ClassEntity classId;

    @Column(name = "created_at")
    private Timestamp createdAt;
}
