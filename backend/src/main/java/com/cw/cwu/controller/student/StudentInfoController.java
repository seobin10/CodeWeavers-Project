package com.cw.cwu.controller.student;

import com.cw.cwu.dto.StudentStatusDTO;
import com.cw.cwu.service.student.StudentInfoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students/info")
@RequiredArgsConstructor
public class StudentInfoController { // 학생 학사 정보 컨트롤러

    private final StudentInfoServiceImpl studentInfoService;

    // 학생의 현재 학년 조회 (학점 기준 계산)
    @GetMapping("/{studentId}/year")
    public ResponseEntity<Integer> getStudentYear(@PathVariable("studentId") String studentId) {
        int year = studentInfoService.calculateStudentYear(studentId);
        return ResponseEntity.ok(year);
    }

    // 학생의 졸업 가능 여부 조회 (130학점 기준)
    @GetMapping("/{studentId}/graduation")
    public ResponseEntity<Boolean> checkGraduation(@PathVariable("studentId") String studentId) {
        boolean isEligible = studentInfoService.checkGraduationEligibility(studentId);
        return ResponseEntity.ok(isEligible);
    }

    //  학생의 학적 상태 + 학년 정보 조회
    @GetMapping("/{studentId}/status")
    public ResponseEntity<StudentStatusDTO> getStudentStatus(@PathVariable("studentId") String studentId) {
        StudentStatusDTO dto = studentInfoService.getStudentStatusInfo(studentId);
        return ResponseEntity.ok(dto);
    }
}
