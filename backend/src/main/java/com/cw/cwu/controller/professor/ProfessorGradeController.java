package com.cw.cwu.controller.professor;

import com.cw.cwu.dto.GradeDetailDTO;
import com.cw.cwu.dto.GradeRegisterDTO;
import com.cw.cwu.dto.PageRequestDTO;
import com.cw.cwu.dto.PageResponseDTO;
import com.cw.cwu.service.professor.ProfessorGradeService;
import com.cw.cwu.util.UserRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/professor/grades")
@RequiredArgsConstructor
public class ProfessorGradeController {

    private final ProfessorGradeService gradeService;
    private final UserRequestUtil userRequestUtil;

    @PostMapping
    public ResponseEntity<String> registerGrade(
            @RequestBody GradeRegisterDTO dto,
            HttpServletRequest request  // 이걸 통해 Authorization 헤더에 접근
    ) throws AccessDeniedException {
        String professorId = userRequestUtil.extractUserId(request);    // 토큰에서 추출된 사용자 ID
        String result = gradeService.registerGrade(dto, professorId);
        return result.contains("완료") ?
                ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

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
}
