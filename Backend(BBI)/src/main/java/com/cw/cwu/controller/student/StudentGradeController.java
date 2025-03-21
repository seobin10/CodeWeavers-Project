package com.cw.cwu.controller.student;

import com.cw.cwu.dto.GradeDTO;
import com.cw.cwu.service.student.StudentGradeService;
import com.cw.cwu.service.student.StudentGradeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students/grade")
@RequiredArgsConstructor
public class StudentGradeController {   // 학생 성적 관리 컨트롤러

    private final StudentGradeService studentGradeService;

    // 학생의 성적 기록 업데이트
    @PostMapping("/{studentId}/update-records")
    public ResponseEntity<String> updateStudentRecords(@PathVariable("studentId") String studentId) {
        studentGradeService.updateStudentRecords(studentId);
        return ResponseEntity.ok("학생 성적 기록이 업데이트되었습니다.");
    }

    // 학생의 성적 조회
    @GetMapping("/{studentId}/grade")
    public ResponseEntity<List<GradeDTO>> getStudentGrade(@PathVariable("studentId") String studentId) {
        return ResponseEntity.ok(studentGradeService.getStudentGrade(studentId));
    }
}