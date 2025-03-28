package com.cw.cwu.controller.admin;

import com.cw.cwu.domain.Department;
import com.cw.cwu.domain.User;
import com.cw.cwu.dto.PageRequestDTO;
import com.cw.cwu.dto.PageResponseDTO;
import com.cw.cwu.dto.UserCreateRequestDTO;
import com.cw.cwu.dto.UserDTO;
import com.cw.cwu.repository.admin.DepartmentRepository;
import com.cw.cwu.repository.user.UserRepository;
import com.cw.cwu.service.admin.AdminUserService;
import com.cw.cwu.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final DepartmentRepository departmentRepository;
    private final FileUploadUtil fileUploadUtil;
    private final UserRepository userRepository;


    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody UserCreateRequestDTO dto) {
        String result = adminUserService.createUser(dto);

        if (!result.equals("성공")) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok("등록이 완료되었습니다.");
    }

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getDepartments() {
        return ResponseEntity.ok(departmentRepository.findAll());
    }

    @PostMapping("/profile")
    public ResponseEntity<String> uploadProfile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = fileUploadUtil.saveFile(file);
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("업로드 실패: " + e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<PageResponseDTO<UserDTO>> getAllUsers(
            PageRequestDTO pageRequestDTO,
            @RequestParam(required = false) String keyword
    ) {
        PageResponseDTO<UserDTO> result = adminUserService.getAllUsers(keyword, pageRequestDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        String result = adminUserService.deleteUser(userId);

        if (!result.equals("사용자가 삭제되었습니다.")) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

}