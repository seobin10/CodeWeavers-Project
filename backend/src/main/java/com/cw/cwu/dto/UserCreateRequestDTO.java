package com.cw.cwu.dto;

import com.cw.cwu.domain.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserCreateRequestDTO {
    private String userId;
    private String userName;
    private String userPassword;
    private String userEmail;
    private String userPhone;
    private LocalDate userBirth;
    private UserRole userRole; // STUDENT, PROFESSOR
    private Integer departmentId;
    private String userImgUrl;
}