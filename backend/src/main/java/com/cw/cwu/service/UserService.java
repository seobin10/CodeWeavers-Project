package com.cw.cwu.service;

import com.cw.cwu.dto.UserDTO;
import com.cw.cwu.domain.User;
import com.cw.cwu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // 로그인
    public UserDTO login(UserDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return convertToDTO(user);
    }

    // 정보 조회
    public UserDTO getUserInfo(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return convertToDTO(user);
    }

    // User 엔티티 -> UserDTO 변환
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setUserBirth(user.getUserBirth());
        dto.setUserEmail(user.getUserEmail());
        dto.setUserPhone(user.getUserPhone());
        dto.setRole(user.getUserRole().toString());
        dto.setDepartmentId(user.getDepartment() != null ? user.getDepartment().getDepartmentId() : null);
        dto.setDepartmentName(user.getDepartment() != null ? user.getDepartment().getDepartmentName() : null);
        return dto;
    }
}
