package com.cw.cwu.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @Column(name = "user_id", length = 9)
    private String userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "user_birth", nullable = false)
    private LocalDate userBirth;

    @Column(name = "user_email", unique = true)
    private String userEmail;

    @Column(name = "user_phone", unique = true)
    private String userPhone;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_img_url")
    private String userImgUrl;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;

    // 교수 이름을 반환하는 메서드
    public String getName() {
        return this.userName;
    }
}