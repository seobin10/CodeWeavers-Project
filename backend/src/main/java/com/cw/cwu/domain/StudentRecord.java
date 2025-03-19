package com.cw.cwu.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_records")
@Getter
@Setter
public class StudentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Integer recordId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(name = "semester", nullable = false, length = 10)
    private String semester;

    @Column(name = "enrolled", nullable = false)
    private int enrolled;

    @Column(name = "earned", nullable = false)
    private int earned;

    @Column(name = "gpa", nullable = false, columnDefinition = "FLOAT")
    private float gpa;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}