package com.cw.cwu.controller;

import com.cw.cwu.service.GradeService;
import com.cw.cwu.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/grade")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;
}
