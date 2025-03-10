package com.green.codeweavers.academymanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// 관리 페이지의 컨트롤러
@Controller
public class ManagementController {
    @ResponseBody
    @GetMapping("/management")
    public String management(){
        return "management";
    }
}
