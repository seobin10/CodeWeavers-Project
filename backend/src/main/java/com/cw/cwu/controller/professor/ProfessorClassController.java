package com.cw.cwu.controller.professor;

import com.cw.cwu.dto.*;
import com.cw.cwu.service.professor.ProfessorClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/professor")
@RequiredArgsConstructor
public class ProfessorClassController {

    private final ProfessorClassService professorClassService;

    @PostMapping("/classes")
    public ResponseEntity<String> createClass(@RequestBody ClassCreateRequestDTO dto) {
        String result = professorClassService.createClass(dto);
        if (!"성공".equals(result)) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok("강의 등록이 완료되었습니다.");
    }

    @GetMapping("/classes")
    public ResponseEntity<PageResponseDTO<ClassDTO>> getMyClasses(
            PageRequestDTO pageRequestDTO,
            @RequestParam String professorId
    ) {
        PageResponseDTO<ClassDTO> result = professorClassService.getMyClasses(professorId, pageRequestDTO);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/classes")
    public ResponseEntity<String> updateClass(@RequestBody ClassUpdateRequestDTO dto) {
        String result = professorClassService.updateClass(dto);
        if (!"수정 완료".equals(result)) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/classes/{classId}")
    public ResponseEntity<String> deleteClass(@PathVariable Integer classId) {
        String result = professorClassService.deleteClass(classId);
        if (!"삭제 완료".equals(result)) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }
}
