package com.cw.cwu.service.admin;

import com.cw.cwu.domain.Department;
import com.cw.cwu.domain.User;
import com.cw.cwu.dto.UserCreateRequestDTO;
import com.cw.cwu.repository.admin.DepartmentRepository;
import com.cw.cwu.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;


    public void createUser(UserCreateRequestDTO dto) {
        if (dto.getDepartmentId() == null) {
            throw new IllegalArgumentException("학과는 반드시 선택해야 합니다.");
        }

        if (userRepository.existsById(dto.getUserId())) {
            throw new IllegalArgumentException("이미 존재하는 사용자 ID입니다.");
        }

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학과입니다."));

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
    }
}