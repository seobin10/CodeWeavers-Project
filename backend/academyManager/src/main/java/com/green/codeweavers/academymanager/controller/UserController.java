package com.green.codeweavers.academymanager.controller;

import com.green.codeweavers.academymanager.dto.UserDTO;
import com.green.codeweavers.academymanager.service.UserService;
import groovy.util.logging.Log4j2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// 교수, 학생 마이페이지의 컨트롤러
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/user")
public class UserController {
    private final UserService service;
    @GetMapping("/{userId}")
    public UserDTO get(@PathVariable(name = "userId") String userId) {
        return service.see(userId);
    }
}
