package com.cw.cwu.controller.user;

import com.cw.cwu.domain.PasswordResetToken;
import com.cw.cwu.domain.User;
import com.cw.cwu.dto.LoginResponseDTO;
import com.cw.cwu.dto.QnADTO;
import com.cw.cwu.dto.QuestionDTO;
import com.cw.cwu.dto.UserDTO;
import com.cw.cwu.repository.PasswordResetTokenRepository;
import com.cw.cwu.service.user.UserServiceImpl;
import com.cw.cwu.util.JWTUtil;
import com.cw.cwu.util.UserRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final UserRequestUtil userRequestUtil;

    @Value("${frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO loginRequestDTO) {
        // 사용자 DB 조회
        User user = userService.findByUserId(loginRequestDTO.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자가 존재하지 않습니다."));
        log.info("user:" +user);
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
        System.out.println("🔍 비밀번호 찾기 요청: " + dto);
        Map<String, String> response = userService.findUserPasswordByUserIdAndEmail(dto.getUserId(), dto.getUserEmail());
        log.info("password:{}",response);
        if (response.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        log.info("response:{}",response);
        return ResponseEntity.ok(response);
    }


    // 사용자 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable("userId") String userId, HttpServletRequest request) {
        String requesterId = userRequestUtil.extractUserId(request);
        return ResponseEntity.ok(userService.getUserInfo(userId, requesterId));
    }

    // 사용자 정보 업데이트 (이메일, 전화번호)
    @PutMapping("/{userId}/update")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable("userId") String userId,
            @RequestBody UserDTO request,
            HttpServletRequest httpRequest) {
        String requesterId = userRequestUtil.extractUserId(httpRequest);
        return ResponseEntity.ok(userService.updateUser(userId, request, requesterId));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<?> changePassword(
            @PathVariable String userId,
            @RequestBody Map<String, String> body,
            HttpServletRequest request) {

        String requesterId = userRequestUtil.extractUserId(request); // JWT에서 사용자 ID 추출
        if (!userId.equals(requesterId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "본인의 비밀번호만 변경할 수 있습니다."));
        }

        String currentPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");

        // 사용자 찾기
        User user = userService.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentPassword, user.getUserPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "현재 비밀번호가 일치하지 않습니다."));
        }

        // 새 비밀번호로 변경
        user.setUserPassword(passwordEncoder.encode(newPassword));
        userService.save(user); // 또는 userRepository.save(user)

        return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 변경되었습니다."));
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
    public Map<String, String> clearText(@PathVariable(name = "questionId") Integer questionId, HttpServletRequest request){
        String requesterId = userRequestUtil.extractUserId(request);
        userService.deleteQna(questionId, requesterId);
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
            @RequestBody QuestionDTO dto,
            HttpServletRequest request) {
        dto.setQuestionId(questionId);
        String requesterId = userRequestUtil.extractUserId(request);
        userService.editQna(dto, requesterId);
        return Map.of("수정 수행 결과", "성공");
    }

    @PostMapping("/send-reset-link")
    public ResponseEntity<?> sendResetLink(@RequestBody UserDTO dto) {
        log.info("비밀번호 reset 요청: {}", dto);

        User user = userService.findByUserId(dto.getUserId()).get();

        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "해당 사용자가 존재하지 않습니다."));
        }


        if (!user.getUserEmail().equals(dto.getUserEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "이메일이 일치하지 않습니다."));
        }

        String token = UUID.randomUUID().toString();
        userService.savePasswordResetToken(user, token);

        String resetLink = frontendUrl + "/reset-password?token=" + token;
        userService.sendResetEmail(dto.getUserEmail(), resetLink);

        return ResponseEntity.ok(Map.of("message", "비밀번호 재설정 링크가 이메일로 전송되었습니다."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("password");
        log.info("🔐 비밀번호 재설정 요청: token={}, newPassword=****", token);

        // 1. 토큰 확인
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 토큰입니다."));

        // 2. 토큰 유효성 확인
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "토큰이 만료되었습니다."));
        }

        // 3. 사용자 비밀번호 변경
        User user = resetToken.getUser();
        user.setUserPassword(passwordEncoder.encode(newPassword));  // ❗ 실제 필드 사용

        // 4. 저장
        userService.save(user);  // ❗

        // 5. 토큰 삭제
        passwordResetTokenRepository.delete(resetToken);

        return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 변경되었습니다."));
    }


}
