package com.cw.cwu.controller;

import com.cw.cwu.dto.GradeDTO;
import com.cw.cwu.repository.UserRepository;
import com.cw.cwu.service.GradeService;
import com.cw.cwu.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grade")
@RequiredArgsConstructor
public class GradeController {
    private final GradeService gradeService;

    @GetMapping("/{studentId}")
    public ResponseEntity<List<GradeDTO>> getStudentGrade(@PathVariable String studentId) {
        return ResponseEntity.ok(gradeService.getStudentGrade(studentId));
    }
}
