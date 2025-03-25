package com.cw.cwu.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Integer enrollment;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Getter
    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity enrolledClassEntity;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    @OneToOne(mappedBy = "enrollment", cascade = CascadeType.ALL)
    private Grade grade;
    
}