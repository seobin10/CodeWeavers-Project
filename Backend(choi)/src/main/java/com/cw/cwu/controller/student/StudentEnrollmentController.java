package com.cw.cwu.controller.student;

import com.cw.cwu.service.student.StudentEnrollmentService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students/enrollment")
@RequiredArgsConstructor
public class StudentEnrollmentController {  // 학생 수강 신청 관리 컨트롤러

    private final StudentEnrollmentService studentEnrollmentService;

    // 학생이 수강 신청 가능한 강의 목록 조회
    @GetMapping("/{studentId}/enrollment")
    public ResponseEntity<List<Map<String, Object>>> getAvailableCourses(
            @PathVariable("studentId") String studentId,
            @RequestParam(required = false) String courseType,
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer courseYear,
            @RequestParam(required = false) String classDay,
            @RequestParam(required = false) Integer classStart,
            @RequestParam(required = false) Integer credit,
            @RequestParam(required = false) String courseName
    ) {
        return ResponseEntity.ok(studentEnrollmentService.getAvailableCourses(studentId, courseType, departmentId, courseYear, classDay, classStart, credit, courseName));
    }
}
