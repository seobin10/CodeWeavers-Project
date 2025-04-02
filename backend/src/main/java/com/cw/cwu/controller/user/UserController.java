package com.cw.cwu.controller.user;

import com.cw.cwu.domain.User;
import com.cw.cwu.dto.LoginResponseDTO;
import com.cw.cwu.dto.QnADTO;
import com.cw.cwu.dto.QuestionDTO;
import com.cw.cwu.dto.UserDTO;
import com.cw.cwu.service.user.UserServiceImpl;
import com.cw.cwu.util.JWTUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO loginRequestDTO) {
        // 사용자 DB 조회
        User user = userService.findByUserId(loginRequestDTO.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자가 존재하지 않습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(loginRequestDTO.getUserPassword(), user.getUserPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "비밀번호가 일치하지 않습니다."));
        }

        // 토큰 발급을 위한 claims 생성
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("email", user.getUserEmail());
        claims.put("name", user.getUserName());
        claims.put("phone", user.getUserPhone());
        claims.put("roleNames", user.getUserRole().name());

        // 토큰 발급
        String accessToken = jwtUtil.generateToken(user.getUserId(), claims, 60); // 60분 유효

        // 응답용 DTO 생성
        UserDTO userDTO = new UserDTO(
                user.getUserId(),
                user.getUserName(),
                user.getUserEmail(),
                null,
                user.getUserPhone(),
                user.getUserRole().name(),
                user.getUserImgUrl()
        );

        return ResponseEntity.ok(new LoginResponseDTO("로그인 성공", accessToken, userDTO));
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        System.out.println(
                "controller logout 들어왔어요 "
        );
        session.invalidate(); // 세션 종료
        return ResponseEntity.ok("로그아웃 완료");
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
    public ResponseEntity<List<QnADTO>> findQnaContent(@PathVariable("questionId") Integer questionId) {
        List<QnADTO> qnaContent = userService.findAnswer(questionId);
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
    public String writeText(@RequestBody QuestionDTO dto, @RequestParam("userId") String userId) {
        Integer questionId = userService.writeQna(dto, userId);
        return "result : " + questionId;
    }

    // 질의응답 게시판 글 삭제
    @DeleteMapping("/qna/delete/{questionId}")
    public Map<String, String> clearText(@PathVariable(name = "questionId") Integer questionId){
        userService.deleteQna(questionId);
        return Map.of("삭제 수행 결과", "성공");
    }

    // 질의응답 아이디 찾기
    @GetMapping("qna/find/{questionId}")
    public ResponseEntity<String> getWriterId(@PathVariable(name = "questionId") Integer questionId) {
        return ResponseEntity.ok(userService.findQnaId(questionId));
    }

    // 질의응답 게시판 글 수정
    @PutMapping("qna/edit/{questionId}")
    public Map<String, String> edited(
            @PathVariable(name = "questionId") Integer questionId,
            @RequestBody QuestionDTO dto) {
        dto.setQuestionId(questionId);
        userService.editQna(dto);
        return Map.of("수정 수행 결과", "성공");
    }
}
