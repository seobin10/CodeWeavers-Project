package com.green.codeweavers.academymanager.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
public class Departments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int departmentId;
    private String departmentName;

    @OneToMany(mappedBy = "department")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "departmentId")
    private List<Courses> courses = new ArrayList<>();
}
