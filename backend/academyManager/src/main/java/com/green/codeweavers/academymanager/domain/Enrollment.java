package com.green.codeweavers.academymanager.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int enrollmentId;
    private Date enrollmentDate;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "user_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Classes classEntity;

    @OneToMany(mappedBy = "enrollmentId")
    private List<Grades> grade = new ArrayList<>();
}
