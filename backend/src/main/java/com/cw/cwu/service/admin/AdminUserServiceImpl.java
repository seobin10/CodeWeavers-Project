package com.cw.cwu.service.admin;

import com.cw.cwu.domain.Department;
import com.cw.cwu.domain.User;
import com.cw.cwu.dto.UserCreateRequestDTO;
import com.cw.cwu.repository.admin.DepartmentRepository;
import com.cw.cwu.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;


    public String createUser(UserCreateRequestDTO dto) {
        if (!dto.getUserId().matches("^\\d{9}$")) {
            return "사용자 ID는 9자리 숫자여야 합니다.";
        }

        if (dto.getDepartmentId() == null) {
            return "학과는 반드시 선택해야 합니다.";
        }

        if (userRepository.existsById(dto.getUserId())) {
            return "이미 존재하는 사용자 ID입니다.";
        }
        if (userRepository.existsByUserEmail(dto.getUserEmail())) {
            return "이미 등록된 이메일입니다.";
        }

        if (userRepository.existsByUserPhone(dto.getUserPhone())) {
            return "이미 등록된 전화번호입니다.";
        }

        if (!dto.getUserPhone().matches("^010-\\d{4}-\\d{4}$")) {
            return "전화번호는 010-XXXX-XXXX 형식이어야 합니다.";
        }

        if (!dto.getUserEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return "이메일 형식이 올바르지 않습니다.";
        }

        if (dto.getUserBirth().isAfter(LocalDate.now())) {
            return "생년월일이 올바르지 않습니다.";
        }

        if (dto.getUserRole() == null) {
            return "사용자 역할은 반드시 지정해야 합니다.";
        }

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElse(null);
        if (department == null) {
            return "존재하지 않는 학과입니다.";
        }

        User user = User.builder()
                .userId(dto.getUserId())
                .userName(dto.getUserName())
                .userPassword(dto.getUserPassword()) // 추후 암호화 필요
                .userEmail(dto.getUserEmail())
                .userPhone(dto.getUserPhone())
                .userBirth(dto.getUserBirth())
                .userRole(dto.getUserRole())
                .department(department)
                .build();

        userRepository.save(user);

        return "성공";
    }
}