package com.cw.cwu.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserUpdateRequestDTO {
    private String userId; // PK니까 수정 불가
    private String userName;
    private LocalDate userBirth;
    private String userEmail;
    private String userPhone;
    private String userRole;
    private Integer departmentId;
    private String userImgUrl;
}

