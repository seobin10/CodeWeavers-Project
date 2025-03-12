package com.green.codeweavers.academymanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// 메인 페이지의 컨트롤러
@Controller
public class MainController {
    @ResponseBody
    @GetMapping("/main")
    public String Main(){
        return "main";
    }

    @GetMapping("/score")
    public String viewScoreTest() {
        return "view_score_test.html";
    }
}
