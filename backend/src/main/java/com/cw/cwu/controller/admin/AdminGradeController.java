package com.cw.cwu.controller.admin;

import com.cw.cwu.service.admin.AdminGradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/grades")
@RequiredArgsConstructor
public class AdminGradeController {

    private final AdminGradeService adminGradeService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/finalize")
    public ResponseEntity<String> finalizeStudentRecordsByDepartment(
            @RequestParam Integer semesterId,
            @RequestParam Integer departmentId
    ) {
        adminGradeService.finalizeStudentRecordsByDepartment(semesterId, departmentId);
        return ResponseEntity.ok("해당 학과의 성적 집계가 완료되었습니다.");
    }
}