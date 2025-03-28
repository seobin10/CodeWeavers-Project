package com.cw.cwu.service.admin;

import com.cw.cwu.domain.Department;
import com.cw.cwu.domain.User;
import com.cw.cwu.domain.UserRole;
import com.cw.cwu.dto.*;
import com.cw.cwu.repository.admin.DepartmentRepository;
import com.cw.cwu.repository.user.UserRepository;
import com.cw.cwu.util.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;


    @Override
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
                .userImgUrl(dto.getUserImgUrl())
                .build();

        userRepository.save(user);

        return "성공";
    }

    @Override
    public PageResponseDTO<UserDTO> getAllUsers(String keyword, PageRequestDTO pageRequestDTO) {
        // 정렬 기준 및 방향 설정
        String sortField = pageRequestDTO.getSortField() != null ? pageRequestDTO.getSortField() : "userId";
        String sortDir = pageRequestDTO.getSortDir() != null ? pageRequestDTO.getSortDir() : "asc";

        if ("departmentName".equals(sortField)) {
            sortField = "department.departmentName";
        }

        // 정렬 객체 생성
        Sort sort = Sort.by(
                "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortField
        );

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                sort
        );

        Page<User> userPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            userPage = userRepository.findByUserIdContainingIgnoreCaseOrUserNameContainingIgnoreCase(
                    keyword, keyword, pageable
            );
        } else {
            userPage = userRepository.findAll(pageable);
        }

        Page<UserDTO> dtoPage = userPage.map(user -> {
            UserDTO dto = new UserDTO();
            dto.setUserId(user.getUserId());
            dto.setUserName(user.getUserName());
            dto.setUserPassword(null);
            dto.setUserBirth(user.getUserBirth());
            dto.setUserEmail(user.getUserEmail());
            dto.setUserPhone(user.getUserPhone());
            dto.setUserRole(user.getUserRole().toString());
            dto.setUserImgUrl(user.getUserImgUrl());

            if (user.getDepartment() != null) {
                dto.setDepartmentId(user.getDepartment().getDepartmentId());
                dto.setDepartmentName(user.getDepartment().getDepartmentName());
            }

            return dto;
        });

        return PageUtil.toDTO(dtoPage, pageRequestDTO.getPage());
    }

    @Override
    public String deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            return "존재하지 않는 사용자입니다.";
        }

        User user = userRepository.findById(userId).orElse(null);

        if (user != null && user.getUserImgUrl() != null) {
            String userImgUrl = user.getUserImgUrl();

            String filePath = "uploads/profiles" + userImgUrl.replace("/uploads/profiles", "");

            File fileToDelete = new File(filePath);

            if (fileToDelete.exists() && fileToDelete.isFile()) {
                if (fileToDelete.delete()) {
                    log.info("프로필 이미지 파일 삭제 성공: " + filePath);
                } else {
                    log.error("프로필 이미지 파일 삭제 실패: " + filePath);
                }
            }
        }

        // 사용자 삭제
        userRepository.deleteById(userId);
        return "사용자가 삭제되었습니다.";
    }

    @Override
    public String updateUser(UserUpdateRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElse(null);
        if (user == null) {
            return "존재하지 않는 사용자입니다.";
        }

        // 필드 업데이트
        user.setUserName(dto.getUserName());
        user.setUserBirth(dto.getUserBirth());
        user.setUserEmail(dto.getUserEmail());
        user.setUserPhone(dto.getUserPhone());
        user.setUserRole(UserRole.valueOf(dto.getUserRole()));
        user.setUserImgUrl(dto.getUserImgUrl());

        if (dto.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(dto.getDepartmentId()).orElse(null);
            user.setDepartment(dept);
        }

        userRepository.save(user);
        return "수정이 완료되었습니다.";
    }

    @Override
    public String resetPassword(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return "존재하지 않는 사용자입니다.";
        }

        if (user.getUserBirth() == null) {
            return "해당 사용자의 생년월일 정보가 없어 초기화할 수 없습니다.";
        }

        String birth = user.getUserBirth().toString(); // yyyy-MM-dd
        String formatted = birth.substring(2, 4) + birth.substring(5, 7) + birth.substring(8, 10); // yyMMdd
        String defaultPassword = formatted + "!";

        user.setUserPassword(defaultPassword); // 추후 passwordEncoder.encode() 예정
        userRepository.save(user);

        return "비밀번호가 초기화되었습니다.";
    }
}