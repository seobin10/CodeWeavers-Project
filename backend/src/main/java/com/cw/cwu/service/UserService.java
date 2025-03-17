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

        // 비밀번호 검증
        if (!user.getUserPassword().equals(request.getUserPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
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

    // 이메일과 전화번호 업데이트
    public UserDTO updateUser(String userId, UserDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.setUserEmail(request.getUserEmail());
        user.setUserPhone(request.getUserPhone());

        userRepository.save(user);

        return convertToDTO(user);
    }

}
