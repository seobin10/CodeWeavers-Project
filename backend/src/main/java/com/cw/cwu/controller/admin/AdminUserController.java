package com.cw.cwu.controller.admin;

import com.cw.cwu.domain.Department;
import com.cw.cwu.dto.UserCreateRequestDTO;
import com.cw.cwu.repository.admin.DepartmentRepository;
import com.cw.cwu.service.admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final DepartmentRepository departmentRepository;

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody UserCreateRequestDTO dto) {
        adminUserService.createUser(dto);
        return ResponseEntity.ok("사용자 생성 완료");
    }

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getDepartments() {
        return ResponseEntity.ok(departmentRepository.findAll());
    }
}
