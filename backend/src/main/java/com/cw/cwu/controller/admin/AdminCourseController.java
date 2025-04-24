package com.cw.cwu.controller.admin;

import com.cw.cwu.dto.CourseCreateRequestDTO;
import com.cw.cwu.dto.CourseInfoDTO;
import com.cw.cwu.dto.CourseUpdateRequestDTO;
import com.cw.cwu.service.admin.AdminCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {

    private final AdminCourseService adminCourseService;


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCoursesByFilter(@RequestParam String departmentId) {
        List<CourseInfoDTO> result = adminCourseService.getCoursesByFilter(departmentId);
        return ResponseEntity.ok(result);
    }

    // 과목 생성
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> createCourse(@RequestBody CourseCreateRequestDTO dto) {
        adminCourseService.createCourse(dto);
        return ResponseEntity.ok("과목이 등록되었습니다.");
    }

    // 과목 수정
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{courseId}")
    public ResponseEntity<String> updateCourse(@PathVariable Integer courseId,
                                               @RequestBody CourseUpdateRequestDTO dto) {
        adminCourseService.updateCourse(courseId, dto);
        return ResponseEntity.ok("과목이 수정되었습니다.");
    }

    // 과목 삭제
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable Integer courseId) {
        adminCourseService.deleteCourse(courseId);
        return ResponseEntity.ok("과목이 삭제되었습니다.");
    }
}
