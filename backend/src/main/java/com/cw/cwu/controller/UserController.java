package com.cw.cwu.controller;

import com.cw.cwu.dto.UserDTO;
import com.cw.cwu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserDTO request) {
        return ResponseEntity.ok(userService.login(request));
    }

    // 사용자 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    // 사용자 정보 업데이트 (이메일, 전화번호)
    @PatchMapping("/{userId}/update")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable String userId,
            @RequestBody UserDTO request) {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }
}
