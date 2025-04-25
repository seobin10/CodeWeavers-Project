package com.cw.cwu.controller.admin;

import com.cw.cwu.dto.DepartmentCreateRequestDTO;
import com.cw.cwu.dto.DepartmentDetailDTO;
import com.cw.cwu.dto.DepartmentUpdateRequestDTO;
import com.cw.cwu.service.admin.AdminDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/departments")
@RequiredArgsConstructor
public class AdminDepartmentController {

    private final AdminDepartmentService adminDepartmentService;

    // 학과 생성
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createDepartment(@RequestBody DepartmentCreateRequestDTO dto) {
        try {
            adminDepartmentService.createDepartment(dto);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // 학과 조회
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<DepartmentDetailDTO>> getDepartment() {
        List<DepartmentDetailDTO> result = adminDepartmentService.getAllDepartments();
        return ResponseEntity.ok(result);

    }


    // 학과 수정
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{departmentId}")
    public ResponseEntity<?> updateDepartment(@PathVariable Integer departmentId,
                                              @RequestBody DepartmentUpdateRequestDTO dto) {
        try {
            adminDepartmentService.updateDepartment(departmentId, dto);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 학과 삭제
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{departmentId}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Integer departmentId) {
        try {
            adminDepartmentService.deleteDepartment(departmentId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
