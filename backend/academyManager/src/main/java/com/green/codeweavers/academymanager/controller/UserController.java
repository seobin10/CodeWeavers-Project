package com.green.codeweavers.academymanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// 교수, 학생 마이페이지의 컨트롤러
@Controller
@RequestMapping("/user")
public class UserController {
    @ResponseBody
    @GetMapping("/professor")
    public String professor() {
        return "professor";
    }
    @ResponseBody
    @GetMapping("/student")
    public String student() {
        return "student";
    }
}
