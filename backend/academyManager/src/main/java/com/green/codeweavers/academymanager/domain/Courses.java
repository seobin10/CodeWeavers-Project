package com.green.codeweavers.academymanager.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
public class Courses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courseId;
    private String courseName;
    @Enumerated(EnumType.STRING)
    private UserRole courseType;
    private int credit;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Departments departmentId;

    @OneToMany(mappedBy = "courseId")
    private List<Classes> classes = new ArrayList<>();
}
