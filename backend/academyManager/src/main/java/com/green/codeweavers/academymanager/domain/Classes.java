package com.green.codeweavers.academymanager.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classes")
public class Classes {
    //    private User user;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int classId;
    private String classSemester;
    private String classSchedule;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Courses courseId;

    @ManyToOne
    @JoinColumn(name = "professor_id", referencedColumnName = "user_id")
    private User professor;

    @OneToMany(mappedBy = "classEntity")
    private List<Enrollment> enrollments = new ArrayList<>();
}
