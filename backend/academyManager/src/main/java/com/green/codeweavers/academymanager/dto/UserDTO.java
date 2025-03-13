package com.green.codeweavers.academymanager.dto;

import com.green.codeweavers.academymanager.domain.Departments;
import com.green.codeweavers.academymanager.domain.UserRole;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String userId;
    private String userName;
    private UserRole userRole;
    private Date userBirth;
    private String userEmail;
    private String userPhone;
    private String userPassword;
    private int department;
}
