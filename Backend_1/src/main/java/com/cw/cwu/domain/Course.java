package com.cw.cwu.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "course_name", unique = true, nullable = false)
    private String courseName;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_type")
    private CourseType courseType;

    @Column(name = "credit")
    private int credit;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department departmentId;
}
