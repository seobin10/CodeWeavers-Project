package com.cw.cwu.controller.admin;

import com.cw.cwu.domain.Department;
import com.cw.cwu.dto.UserCreateRequestDTO;
import com.cw.cwu.repository.admin.DepartmentRepository;
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
}