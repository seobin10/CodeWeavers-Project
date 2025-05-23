package com.cw.cwu.controller.professor;

import com.cw.cwu.domain.ScheduleType;
import com.cw.cwu.dto.GradeDetailDTO;
import com.cw.cwu.dto.GradeRegisterDTO;
import com.cw.cwu.dto.PageRequestDTO;
import com.cw.cwu.dto.PageResponseDTO;
import com.cw.cwu.service.admin.AdminScheduleService;
import com.cw.cwu.service.professor.ProfessorGradeService;
import com.cw.cwu.util.UserRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/professor/grades")
@RequiredArgsConstructor
public class ProfessorGradeController {

    private final ProfessorGradeService gradeService;
    private final UserRequestUtil userRequestUtil;
    private final AdminScheduleService adminScheduleService;

    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping
    public ResponseEntity<String> registerGrade(
            @RequestBody GradeRegisterDTO dto,
            HttpServletRequest request
    ) throws AccessDeniedException {
        String professorId = userRequestUtil.extractUserId(request);
        String result = gradeService.registerGrade(dto, professorId);
        return result.contains("완료") ?
                ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping
    public ResponseEntity<String> updateGrade(
            @RequestBody GradeRegisterDTO dto,
            HttpServletRequest request
    ) throws AccessDeniedException {
        String professorId = userRequestUtil.extractUserId(request);
        String result = gradeService.updateGrade(dto, professorId);
        return result.contains("완료") ?
                ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("/{gradeId}")
    public ResponseEntity<String> deleteGrade(
            @PathVariable Integer gradeId,
            HttpServletRequest request
    ) throws AccessDeniedException {
        String professorId = userRequestUtil.extractUserId(request);
        String result = gradeService.deleteGrade(gradeId, professorId);
        return result.contains("완료") ?
                ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @PreAuthorize("hasRole('PROFESSOR')")
    @GetMapping("/class/{classId}")
    public ResponseEntity<PageResponseDTO<GradeDetailDTO>> getGradesByClass(
            @PathVariable Integer classId,
            PageRequestDTO pageRequestDTO,
            HttpServletRequest request
    ) throws AccessDeniedException {

        String professorId = userRequestUtil.extractUserId(request);
        PageResponseDTO<GradeDetailDTO> response = gradeService.getGradesByClass(professorId, classId, pageRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/is-grade-open")
    public ResponseEntity<Boolean> isGradeOpen() {
        boolean result = adminScheduleService.isScheduleOpen(ScheduleType.GRADE);
        return ResponseEntity.ok(result);
    }
}
