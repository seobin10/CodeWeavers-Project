package com.cw.cwu.controller.admin;

import com.cw.cwu.dto.DepartmentDetailDTO;
import com.cw.cwu.service.admin.AdminDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/departments")
@RequiredArgsConstructor
public class AdminDepartmentController {

    private final AdminDepartmentService adminDepartmentService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{departmentId}")
    public ResponseEntity<?> getDepartmentDetail(@PathVariable Integer departmentId) {
        try {
            DepartmentDetailDTO detail = adminDepartmentService.getDepartmentDetail(departmentId);
            return ResponseEntity.ok(detail);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}