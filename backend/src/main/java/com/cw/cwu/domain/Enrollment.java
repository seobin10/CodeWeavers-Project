package com.cw.cwu.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassEntity enrolledClassEntity;

    @OneToOne(mappedBy = "enrollment", cascade = CascadeType.ALL)
    private Grade grade;

    public ClassEntity getEnrolledClassEntity() {
        return enrolledClassEntity;
    }
}