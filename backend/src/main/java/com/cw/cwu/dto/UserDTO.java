package com.cw.cwu.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String userId; // 사용자ID(학번or교수관리자코드)
    private String userName; // 이름
    private String userPassword;
    private LocalDate userBirth; // 생년월일
    private String userEmail; // 이메일
    private String userPhone; // 전화번호
    private String userRole; // STUDENT, PROFESSOR, ADMIN 구분
    private Integer departmentId; // 학과 ID
    private String departmentName; // 학과명
    private String userImgUrl;
    public UserDTO(String userId, String userName, String userEmail, String userPassword,
                   String userPhone, String userRole, String userImgUrl) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userPhone = userPhone;
        this.userRole = userRole;
        this.userImgUrl = userImgUrl;
    }
    public Map<String, Object> getClaims() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("userId", this.userId);
        dataMap.put("email", this.userEmail);
        dataMap.put("password", this.userPassword);
        dataMap.put("phone", this.userPhone);
        dataMap.put("name", this.userName);
        dataMap.put("roleNames", this.userRole != null ? this.userRole : "UNKNOWN");
        return dataMap;
    }

}