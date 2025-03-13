package com.cw.cwu.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserDTO {
    private String userId; // 사용자ID(학번or교수관리자코드)
    private String userName; // 이름
    private LocalDate userBirth; // 생년월일
    private String userEmail; // 이메일
    private String userPhone; // 전화번호
    private String role; // STUDENT, PROFESSOR, ADMIN 구분
    private Integer departmentId; // 학과 ID
    private String departmentName; // 학과명
}
