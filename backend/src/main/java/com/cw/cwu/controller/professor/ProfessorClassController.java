package com.cw.cwu.controller.professor;

import com.cw.cwu.dto.*;
import com.cw.cwu.service.professor.ProfessorClassService;
import com.cw.cwu.util.UserRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professor")
@RequiredArgsConstructor
public class ProfessorClassController {

    private final ProfessorClassService professorClassService;
    private final UserRequestUtil userRequestUtil;

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
            @RequestParam(required = false) Integer semesterId,
            HttpServletRequest request
    ) {
        String professorId = userRequestUtil.extractUserId(request);
        PageResponseDTO<ClassDTO> result = professorClassService.getMyClasses(professorId, pageRequestDTO, semesterId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/classes")
    public ResponseEntity<String> updateClass(@RequestBody ClassUpdateRequestDTO dto, HttpServletRequest request) {
        String professorId = userRequestUtil.extractUserId(request);
        String result = professorClassService.updateClass(dto, professorId);
        if (!"수정 완료".equals(result)) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/classes/{classId}")
    public ResponseEntity<String> deleteClass(
            @PathVariable Integer classId,
            HttpServletRequest request
    ) {
        String professorId = userRequestUtil.extractUserId(request);
        String result = professorClassService.deleteClass(classId, professorId);

        if (!"삭제 완료".equals(result)) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseSimpleDTO>> getCoursesByProfessor(HttpServletRequest request) {
        String professorId = userRequestUtil.extractUserId(request); // 토큰에서 교수 ID 추출
        List<CourseSimpleDTO> list = professorClassService.getCoursesByProfessor(professorId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/lecture-rooms")
    public ResponseEntity<List<LectureRoomSimpleDTO>> getAvailableLectureRooms(
            @RequestParam String day,
            @RequestParam int startTime,
            @RequestParam int endTime
    ) {
        List<LectureRoomSimpleDTO> available = professorClassService.getAvailableLectureRooms(
                day, startTime, endTime
        );
        return ResponseEntity.ok(available);
    }
}
