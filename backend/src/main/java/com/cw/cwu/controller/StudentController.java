package com.cw.cwu.controller;

import com.cw.cwu.dto.GradeDTO;
import com.cw.cwu.service.StudentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentServiceImpl studentServiceimpl;

    // 학생의 성적 기록 업데이트
    @PostMapping("/{studentId}/update-records")
    public ResponseEntity<String> updateStudentRecords(@PathVariable String studentId) {
        studentServiceimpl.updateStudentRecords(studentId);
        return ResponseEntity.ok("학생 성적 기록이 업데이트되었습니다.");
    }

    // 학생의 현재 학년을 조회 - 학점기준 계산함
    @GetMapping("/{studentId}/year")
    public ResponseEntity<Integer> getStudentYear(@PathVariable String studentId) {
        int year = studentServiceimpl.calculateStudentYear(studentId);
        return ResponseEntity.ok(year);
    }

    // 학생의 졸업 가능 여부 조회 - 130학점기준
    @GetMapping("/{studentId}/graduation")
    public ResponseEntity<Boolean> checkGraduation(@PathVariable String studentId) {
        boolean isEligible = studentServiceimpl.checkGraduationEligibility(studentId);
        return ResponseEntity.ok(isEligible);
    }

    // 성적 조회
    @GetMapping("/{studentId}/grade")
    public ResponseEntity<List<GradeDTO>> getStudentGrade(@PathVariable String studentId) {
        return ResponseEntity.ok(studentServiceimpl.getStudentGrade(studentId));
    }

    // 학생이 수강 신청 가능한 강의 목록 조회
    @GetMapping("/{studentId}/enrollment")
    public ResponseEntity<List<Map<String, Object>>> getAvailableCourses(
            @PathVariable String studentId,
            @RequestParam(required = false) String courseType,
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer courseYear,
            @RequestParam(required = false) String classDay,
            @RequestParam(required = false) Integer classStart,
            @RequestParam(required = false) Integer credit,
            @RequestParam(required = false) String courseName
    ) {
        return ResponseEntity.ok(studentServiceimpl.getAvailableCourses(studentId, courseType, departmentId, courseYear, classDay, classStart, credit, courseName));
    }

}