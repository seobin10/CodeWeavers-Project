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
    public ResponseEntity<String> finalizeCurrentSemesterRecords(
            @RequestParam Integer departmentId
    ) {
        try {
            Integer semesterId = adminGradeService.getCurrentSemesterDTO().getSemesterId();
            adminGradeService.finalizeStudentRecordsByDepartment(semesterId, departmentId);
            return ResponseEntity.ok("현재 학기의 성적 집계가 완료되었습니다.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/summary")
    public ResponseEntity<?> getCurrentGradeStatusSummary(
            @RequestParam Integer departmentId
    ) {
        Integer semesterId = adminGradeService.getCurrentSemesterDTO().getSemesterId();
        return ResponseEntity.ok(
                adminGradeService.getGradeStatusSummary(semesterId, departmentId)
        );
    }
    // userInfo 학적상태 처리 위해 권한 제거
    // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/current-semester")
    public ResponseEntity<?> getCurrentSemester() {
        return ResponseEntity.ok(adminGradeService.getCurrentSemesterDTO());
    }

}