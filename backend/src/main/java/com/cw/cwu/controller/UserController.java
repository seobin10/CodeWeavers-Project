package com.cw.cwu.controller;

import com.cw.cwu.dto.UserDTO;
import com.cw.cwu.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserDTO dto) {
        System.out.println("login controller: " +dto);
        return ResponseEntity.ok(userService.login(dto));
    }

    @PostMapping("/finduserId")
    public ResponseEntity<String> findId(@RequestBody UserDTO dto){
        System.out.println("find userid controller : " +dto);
        String userid= userService.findUserIdByUserName(dto.getUserName());
        System.out.println("db에서 찾은  user_id "+userid);
        return ResponseEntity.ok(userid);
    }

    // 사용자 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    // 사용자 정보 업데이트 (이메일, 전화번호)
    @PutMapping("/{userId}/update")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable String userId,
            @RequestBody UserDTO request) {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }


}
