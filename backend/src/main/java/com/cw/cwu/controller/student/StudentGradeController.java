package com.cw.cwu.controller.student;

import com.cw.cwu.domain.StudentRecord;
import com.cw.cwu.dto.GradeDTO;
import com.cw.cwu.dto.TotalRecordDTO;
import com.cw.cwu.service.student.StudentGradeService;
import com.cw.cwu.util.UserRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/students/grade")
@RequiredArgsConstructor
public class StudentGradeController {   // 학생 성적 관리 컨트롤러

    private final StudentGradeService studentGradeService;
    private final UserRequestUtil userRequestUtil;


    // 학생의 성적 조회
    @GetMapping("")
    public ResponseEntity<List<GradeDTO>> getStudentGrade(HttpServletRequest request) {
        String studentId = userRequestUtil.extractUserId(request);
        return ResponseEntity.ok(studentGradeService.getStudentGrade(studentId));
    }

    // 학생의 현재 학기 총합 성적(신청학점, 취득학점, GPA) 조회
    @GetMapping("/record")
    public ResponseEntity<StudentRecord> getStudentRecord(HttpServletRequest request) {
        String studentId = userRequestUtil.extractUserId(request);
        return ResponseEntity.ok(studentGradeService.getStudentRecord(studentId));
    }

    // 학생의 특정 학기 성적 조회
    @GetMapping("/semester")
    public ResponseEntity<List<GradeDTO>> getStudentGradeBySemester(
            HttpServletRequest request,
            @RequestParam("semesterId") Integer semesterId
    ) {
        String studentId = userRequestUtil.extractUserId(request);
        return ResponseEntity.ok(studentGradeService.getStudentGradeBySemester(studentId, semesterId));
    }

    // 학생의 전체 학기 총합 성적(레코드) 조회
    @GetMapping("/all-records")
    public ResponseEntity<List<StudentRecord>> getAllStudentRecords(HttpServletRequest request) {
        String studentId = userRequestUtil.extractUserId(request);
        return ResponseEntity.ok(studentGradeService.getAllStudentRecords(studentId));
    }

    // 학생의 전체 누적 총합 성적(총 취득학점, 총 평균평점) 조회
    @GetMapping("/total-record")
    public ResponseEntity<TotalRecordDTO> getTotalRecord(HttpServletRequest request) {
        String studentId = userRequestUtil.extractUserId(request);
        return ResponseEntity.ok(studentGradeService.getTotalRecord(studentId));
    }
}