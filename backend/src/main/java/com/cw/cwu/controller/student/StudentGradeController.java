package com.cw.cwu.controller.student;

import com.cw.cwu.dto.GradeDTO;
import com.cw.cwu.service.student.StudentGradeService;
import com.cw.cwu.service.student.StudentGradeServiceImpl;
import com.cw.cwu.util.UserRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students/grade")
@RequiredArgsConstructor
public class StudentGradeController {   // 학생 성적 관리 컨트롤러

    private final StudentGradeService studentGradeService;
    private final UserRequestUtil userRequestUtil;

    // 학생의 성적 기록 업데이트
    @PostMapping("/update-records")
    public ResponseEntity<String> updateStudentRecords(HttpServletRequest request) {
        String studentId = userRequestUtil.extractUserId(request);
        studentGradeService.updateStudentRecords(studentId, studentId);
        return ResponseEntity.ok("학생 성적 기록이 업데이트되었습니다.");
    }

    // 학생의 성적 조회
    @GetMapping("/grade")
    public ResponseEntity<List<GradeDTO>> getStudentGrade(HttpServletRequest request) {
        String studentId = userRequestUtil.extractUserId(request);
        return ResponseEntity.ok(studentGradeService.getStudentGrade(studentId));
    }
}