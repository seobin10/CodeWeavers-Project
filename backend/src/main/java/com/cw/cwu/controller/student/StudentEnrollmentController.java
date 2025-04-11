package com.cw.cwu.controller.student;

import com.cw.cwu.domain.ScheduleType;
import com.cw.cwu.dto.EnrollmentRequestDTO;
import com.cw.cwu.dto.PageRequestDTO;
import com.cw.cwu.dto.PageResponseDTO;
import com.cw.cwu.service.admin.AdminScheduleService;
import com.cw.cwu.service.student.StudentEnrollmentService;

import com.cw.cwu.util.UserRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/students/enrollment")
@RequiredArgsConstructor
public class StudentEnrollmentController {  // 학생 수강 신청 관리 컨트롤러

    private final StudentEnrollmentService studentEnrollmentService;
    private final AdminScheduleService adminScheduleService;
    private final UserRequestUtil userRequestUtil;

    // 학생이 수강 신청 가능한 강의 목록 조회
    @GetMapping("/{studentId}/enrollment")
    public ResponseEntity<PageResponseDTO<Map<String, Object>>> getAvailableCourses(
            @PathVariable("studentId") String studentId,
            @ModelAttribute PageRequestDTO pageRequest,
            @RequestParam(required = false) String courseType,
            @RequestParam(required = false) String departmentName,
            @RequestParam(required = false) Integer courseYear,
            @RequestParam(required = false) String classDay,
            @RequestParam(required = false) Integer classStart,
            @RequestParam(required = false) Integer credit,
            @RequestParam(required = false) String courseName
    ) {
        return ResponseEntity.ok(
                studentEnrollmentService.getAvailableCoursesPaged(
                        studentId, courseType, departmentName, courseYear,
                        classDay, classStart, credit, courseName,
                        pageRequest
                )
        );
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
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/{studentId}/enrollment")
    public ResponseEntity<String> enroll(@RequestBody EnrollmentRequestDTO requestDTO, HttpServletRequest request) {
        System.out.println("등록  controller ");
        String studentId = userRequestUtil.extractUserId(request);
        String result=  studentEnrollmentService.applyToClass(requestDTO, studentId);
        System.out.println("result controller:"+result);
        return ResponseEntity.ok(result);
    }

    // 임시 수강 목록 조회
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/{studentId}/mycourses")
    public ResponseEntity<List<Map<String, Object>>> getMyCourses(@PathVariable String studentId) {
        return ResponseEntity.ok(studentEnrollmentService.getMyCourses(studentId));
    }

    // 강의 취소
    @PreAuthorize("hasRole('STUDENT')")
    @DeleteMapping("/{studentId}/course/{classId}")
    public ResponseEntity<String> deleteCourse(
            @PathVariable String studentId,
            @PathVariable Integer classId,
            HttpServletRequest request) {
        // 요청 로그
        System.out.println("강의 삭제: studentId=" + studentId + ", classId=" + classId);
        String requesterId = userRequestUtil.extractUserId(request);
        String result = studentEnrollmentService.deleteCourse(studentId, classId, requesterId);

        // 응답 로그
        System.out.println(" 응답 " + result);
        return ResponseEntity.ok(result);
    }

    // 수강 신청 내역 조회
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/{studentId}/history")
    public ResponseEntity<List<Map<String, Object>>> getEnrolledCourses(@PathVariable String studentId) {
        return ResponseEntity.ok(studentEnrollmentService.getConfirmedCourses(studentId));
    }

    @GetMapping("/is-enroll-open")
    public ResponseEntity<Boolean> isEnrollOpen() {
        boolean result = adminScheduleService.isScheduleOpen(ScheduleType.ENROLL);
        return ResponseEntity.ok(result);
    }
}