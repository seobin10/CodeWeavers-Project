package com.cw.cwu.controller.user;

import com.cw.cwu.dto.AnswerDTO;
import com.cw.cwu.dto.QuestionDTO;
import com.cw.cwu.dto.UserDTO;
import com.cw.cwu.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    // 학번 찾기
    @PostMapping("/finduserId")
    public ResponseEntity<String> findId(@RequestBody UserDTO dto){
        System.out.println("find userid controller : " +dto);
        String userid= userService.findUserIdByUserName(dto.getUserName());
        System.out.println("db에서 찾은  user_id "+userid);
        return ResponseEntity.ok(userid);
    }

    // pw 찾기
    @PostMapping("/finduserPassword")
    public ResponseEntity<Map<String, String>> findPassword(@RequestBody UserDTO dto) {
        System.out.println("find userpassword controller : " + dto);
        Map<String, String> response = userService.findUserPasswordByUserIdAndEmail(dto.getUserId(), dto.getUserEmail());
        if (response.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    // 사용자 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    // 사용자 정보 업데이트 (이메일, 전화번호)
    @PutMapping("/{userId}/update")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable("userId") String userId,
            @RequestBody UserDTO request) {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    // 질의응답 게시판 리스트를 출력
    @GetMapping("/qna/list")
    public ResponseEntity<List<QuestionDTO>> findQnaList() {
        List<QuestionDTO> qnaList = userService.findAllQna();
        return ResponseEntity.ok(qnaList);
    }

    //질의응답 게시판 내용을 출력
    @GetMapping("/qna/{questionId}")
    public ResponseEntity<List<AnswerDTO>> findQnaContent(@PathVariable("questionId") Integer questionId) {
        List<AnswerDTO> qnaContent = userService.findAnswer(questionId);
        return ResponseEntity.ok(qnaContent);
    }

    // 조회수 업데이트
    @PutMapping("/qna/{questionId}/update")
    public ResponseEntity<String> updateView(@PathVariable("questionId") Integer questionId) {
        userService.updateCount(questionId);
        return ResponseEntity.ok("조회수 업데이트가 완료되었습니다.");
    }

    // 질의응답 게시판 글 작성
    @PostMapping("/qna/write")
    public String writeText(@RequestBody QuestionDTO dto) {
        Integer questionId = userService.writeQna(dto);
        return "result : " + questionId;
    }
}
