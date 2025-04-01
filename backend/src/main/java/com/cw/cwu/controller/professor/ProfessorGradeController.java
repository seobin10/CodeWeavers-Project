package com.cw.cwu.controller.professor;

import com.cw.cwu.dto.GradeDetailDTO;
import com.cw.cwu.dto.GradeRegisterDTO;
import com.cw.cwu.dto.PageRequestDTO;
import com.cw.cwu.dto.PageResponseDTO;
import com.cw.cwu.service.professor.ProfessorGradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professor/grades")
@RequiredArgsConstructor
public class ProfessorGradeController {

    private final ProfessorGradeService gradeService;

    @PostMapping
    public ResponseEntity<String> registerGrade(@RequestBody GradeRegisterDTO dto) {
        String result = gradeService.registerGrade(dto);
        return result.contains("완료") ?
                ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @PutMapping
    public ResponseEntity<String> updateGrade(@RequestBody GradeRegisterDTO dto) {
        String result = gradeService.updateGrade(dto);
        return result.contains("완료") ?
                ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping("/{gradeId}")
    public ResponseEntity<String> deleteGrade(@PathVariable Integer gradeId) {
        String result = gradeService.deleteGrade(gradeId);
        return result.contains("완료") ?
                ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<PageResponseDTO<GradeDetailDTO>> getGradesByClass(
            @PathVariable Integer classId,
            PageRequestDTO pageRequestDTO // 자동으로 쿼리 파라미터 바인딩됨
    ) {
        return ResponseEntity.ok(gradeService.getGradesByClass(classId, pageRequestDTO));
    }
}
