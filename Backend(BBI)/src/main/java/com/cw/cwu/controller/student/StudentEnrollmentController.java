package com.cw.cwu.controller.student;

import com.cw.cwu.dto.EnrollmentRequestDTO;
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
            @RequestParam(required = false) String departmentName,
            @RequestParam(required = false) Integer courseYear,
            @RequestParam(required = false) String classDay,
            @RequestParam(required = false) Integer classStart,
            @RequestParam(required = false) Integer credit,
            @RequestParam(required = false) String courseName
    ) {
        return ResponseEntity.ok(studentEnrollmentService.getAvailableCourses(studentId, courseType, departmentName, courseYear, classDay, classStart, credit, courseName));
    }

    // 필터 옵션 조회
    @GetMapping("/departments")
    public ResponseEntity<List<Map<String, Object>>> getDepartments() {
        return ResponseEntity.ok(studentEnrollmentService.getDepartments());
    }

    @GetMapping("/courseTypes")
    public ResponseEntity<List<Map<String, Object>>> getCourseTypes() {
        return ResponseEntity.ok(studentEnrollmentService.getCourseTypes());
    }

    @GetMapping("/courseYears")
    public ResponseEntity<List<Map<String, Object>>> getCourseYears() {
        return ResponseEntity.ok(studentEnrollmentService.getCourseYears());
    }

    @GetMapping("/classDays")
    public ResponseEntity<List<Map<String, Object>>> getClassDays() {
        return ResponseEntity.ok(studentEnrollmentService.getClassDays());
    }

    @GetMapping("/classTimes")
    public ResponseEntity<List<Map<String, Object>>> getClassTimes() {
        return ResponseEntity.ok(studentEnrollmentService.getClassTimes());
    }

    @GetMapping("/credits")
    public ResponseEntity<List<Map<String, Object>>> getCredits() {
        return ResponseEntity.ok(studentEnrollmentService.getCredits());
    }

    // 수강 신청 요청
    @PostMapping("/{studentId}/enrollment")
    public ResponseEntity<String> enroll(@RequestBody EnrollmentRequestDTO requestDTO) {
        System.out.println("등록 controller ");
        String result = studentEnrollmentService.applyToClass(requestDTO);
        System.out.println("result controller:" +result);
        return ResponseEntity.ok(result);
    }

    // 임시 수강 목록 조회
    @GetMapping("/{studentId}/mycourses")
    public ResponseEntity<List<Map<String, Object>>> getMyCourses(@PathVariable String studentId) {
        return ResponseEntity.ok(studentEnrollmentService.getMyCourses(studentId));
    }

    // 강의 삭제
    @DeleteMapping("/{studentId}/course/{classId}")
    public ResponseEntity<String> deleteCourse(
            @PathVariable String studentId,
            @PathVariable Integer classId) {
        // 요청 로그
        System.out.println("강의 삭제: studentId=" + studentId + ", classId=" + classId);
        String result = studentEnrollmentService.deleteCourse(studentId, classId);

        // 응답 로그
        System.out.println(" 응답 " + result);
        return ResponseEntity.ok(result);
//    }
       // return ResponseEntity.ok(studentEnrollmentService.deleteCourse(studentId, classId));
    }

    // 수강 신청 내역 조회
    @GetMapping("/{studentId}/history")
    public ResponseEntity<List<Map<String, Object>>> getEnrolledCourses(@PathVariable String studentId) {
        return ResponseEntity.ok(studentEnrollmentService.getConfirmedCourses(studentId));
    }


}