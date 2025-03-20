package com.cw.cwu.service;

import com.cw.cwu.domain.User;
import com.cw.cwu.dto.UserDTO;

public interface UserService {
    public UserDTO login(UserDTO request);
    public UserDTO getUserInfo(String userId);
    public String findUserIdByUserName(String username);

    // User 엔티티 -> UserDTO 변환
    default UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setUserPassword(user.getUserPassword());
        dto.setUserBirth(user.getUserBirth());
        dto.setUserEmail(user.getUserEmail());
        dto.setUserPhone(user.getUserPhone());
        dto.setRole(user.getUserRole().toString());
        dto.setUserImgUrl(user.getUserImgUrl());
        dto.setDepartmentId(user.getDepartment() != null ? user.getDepartment().getDepartmentId() : null);
        dto.setDepartmentName(user.getDepartment() != null ? user.getDepartment().getDepartmentName() : null);
        return dto;
    }

}
