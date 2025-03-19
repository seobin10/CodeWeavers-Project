package com.cw.cwu.controller;

import com.cw.cwu.dto.GradeDTO;
import com.cw.cwu.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // 학생의 성적 기록 업데이트
    @PostMapping("/{studentId}/update-records")
    public ResponseEntity<String> updateStudentRecords(@PathVariable String studentId) {
        studentService.updateStudentRecords(studentId);
        return ResponseEntity.ok("학생 성적 기록이 업데이트되었습니다.");
    }

    // 학생의 현재 학년을 조회 - 학점기준 계산함
    @GetMapping("/{studentId}/year")
    public ResponseEntity<Integer> getStudentYear(@PathVariable String studentId) {
        int year = studentService.calculateStudentYear(studentId);
        return ResponseEntity.ok(year);
    }

    // 학생의 졸업 가능 여부 조회 - 130학점기준
    @GetMapping("/{studentId}/graduation")
    public ResponseEntity<Boolean> checkGraduation(@PathVariable String studentId) {
        boolean isEligible = studentService.checkGraduationEligibility(studentId);
        return ResponseEntity.ok(isEligible);
    }

    // 성적 조회
    @GetMapping("/{studentId}/grade")
    public ResponseEntity<List<GradeDTO>> getStudentGrade(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.getStudentGrade(studentId));
    }
}