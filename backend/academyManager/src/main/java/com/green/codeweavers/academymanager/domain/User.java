package com.green.codeweavers.academymanager.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class User {
    // 학생과 교수의 기능, 비즈니로직을 정의하는 영역
    @Id
    @Column(name = "user_id", length = 9)
    private String userId;
    private String userName;
    private UserRole userRole;
    private Date userBirth;
    private String userEmail;
    private String userPhone;
    private String userPassword;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Departments department;

    @OneToMany(mappedBy = "professor")
    @Builder.Default
    private List<Classes> classes = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    @Builder.Default
    private List<Enrollment> enrollments = new ArrayList<>();
}

