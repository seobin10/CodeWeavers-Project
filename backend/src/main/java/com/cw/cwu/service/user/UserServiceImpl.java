package com.cw.cwu.service.user;

import com.cw.cwu.domain.PasswordResetToken;
import com.cw.cwu.domain.Question;
import com.cw.cwu.domain.Status;
import com.cw.cwu.dto.QnADTO;
import com.cw.cwu.dto.QuestionDTO;
import com.cw.cwu.dto.UserDTO;
import com.cw.cwu.domain.User;
import com.cw.cwu.repository.PasswordResetTokenRepository;
import com.cw.cwu.repository.QuestionRepository;
import com.cw.cwu.repository.UserRepository;
import com.cw.cwu.util.AuthUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final QuestionRepository qnaRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JavaMailSender mailSender;

    // 로그인
    @Override
    public UserDTO login(UserDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        // 비밀번호 검증
        if (!user.getUserPassword().equals(request.getUserPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        System.out.println("service user userId:" +userId);
        return userRepository.findByUserId(userId);
    }

    // 정보 조회
    @Override
    public UserDTO getUserInfo(String userId, String requesterId) {
        AuthUtil.checkOwnership(userId, requesterId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        return modelMapper.map(user, UserDTO.class);
    }

    // 학번 찾기
    @Override
    public String findUserIdByUserName(String username) {
        System.out.println("service user name 으로 userid(학번) 찾기 :  " + username);
        return userRepository.findUserIdByUserName(username).get();
    }

    // ✅ 비밀번호 재설정 토큰 저장
    @Override
    public void savePasswordResetToken(User user, String token) {
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(30))  // 30분 유효기간
                .build();

        passwordResetTokenRepository.save(resetToken);
    }

    // ✅ 재설정 이메일 전송
    @Override
    public void sendResetEmail(String toEmail, String resetLink) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("비밀번호 재설정 링크");

            String htmlContent = "<p>아래 링크를 클릭하여 비밀번호를 재설정하세요:</p>"
                    + "<a href=\"" + resetLink + "\">비밀번호 재설정</a>";

            helper.setText(htmlContent, true);  // HTML true로 설정
            helper.setFrom("eonuniversity@naver.com");

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    // pw 찾기
    @Override
    public Map<String, String> findUserPasswordByUserIdAndEmail(String userId, String userEmail) {
        Optional<User> optionalUser = userRepository.findByUserIdAndUserEmail(userId, userEmail);

        Map<String, String> response = new HashMap<>();

        if (optionalUser.isEmpty()) {
            response.put("error", "해당 정보를 가진 사용자를 찾을 수 없습니다.");
            return response;
        }

        String token = UUID.randomUUID().toString();
        User user = optionalUser.get();
        // ✅ 기존 토큰 제거
        passwordResetTokenRepository.deleteByUser(user);
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .build();

        passwordResetTokenRepository.save(resetToken);

        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        sendResetEmail(user.getUserEmail(), resetLink);

        response.put("message", "비밀번호 재설정 링크가 이메일로 전송되었습니다.");
        response.put("email", user.getUserEmail());
        return response;
    }


    // 이메일과 전화번호 업데이트
    @Override
    public UserDTO updateUser(String userId, UserDTO request, String requesterId) {
        AuthUtil.checkOwnership(userId, requesterId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        modelMapper.getConfiguration().setPropertyCondition(context -> context.getSource() != null);
        modelMapper.map(request, user);

        userRepository.save(user);

        return modelMapper.map(user, UserDTO.class);
    }

    // QnA 게시판 리스트를 조회
    @Override
    public List<QuestionDTO> findAllQna() {
        return qnaRepository.findAllQna()
                .stream()
                .map(row -> {
                    LocalDate createdAt;
                    if (row[4] instanceof java.sql.Date) {
                        createdAt = ((java.sql.Date) row[4]).toLocalDate();
                    } else if (row[4] instanceof String) {
                        createdAt = LocalDate.parse((String) row[4]);
                    } else {
                        throw new RuntimeException("Unexpected type for createdAt: " + row[4].getClass().getName());
                    }
                    QuestionDTO dto = new QuestionDTO(
                            ((Integer) row[0]),
                            (String) row[1],
                            (String) row[2],
                            (String) row[3],
                            createdAt,
                            (String) row[5],
                            ((Integer) row[6])
                    );
                    dto.setStatus(dto.statusToString(Status.valueOf(dto.getStatus())));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // QnA 내용을 조회
    @Override
    public List<QnADTO> findAnswer(Integer questionId) {
        return qnaRepository.findAnswer(questionId)
                .stream()
                .map(row -> new QnADTO(
                        ((Integer) row[0]),
                        ((Integer) row[1]),
                        (String) row[2],
                        (String) row[3],
                        ((java.sql.Date) row[4]).toLocalDate(),
                        (String) row[5],
                        (String) row[6]
                ))
                .collect(Collectors.toList());
    }

    // 조회수 업데이트
    @Override
    public QuestionDTO updateCount(Integer questionId) {
        Question question = qnaRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("데이터를 찾을 수 없습니다."));
        int updateCount = question.getViewCount() + 1;
        question.setViewCount(updateCount);

        qnaRepository.save(question);

        QuestionDTO dto = new QuestionDTO();
        dto.setQuestionId(question.getQuestionId());
        dto.setTitle(question.getTitle());
        dto.setUserName(String.valueOf(question.getUserId()));
        dto.setCreatedAt(question.getQuestionDate());
        dto.setStatus(String.valueOf(question.getStatus()));

        return dto;
    }

    // Q&A 작성
    @Override
    public Integer writeQna(QuestionDTO dto, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("userId 데이터를 찾을 수 없습니다."));

        Question question = Question.builder()
                .questionId(dto.getQuestionId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .userId(user)
                .questionDate(dto.getCreatedAt())
                .status(dto.statusToEnum(dto.getStatus()))
                .viewCount(dto.getViewCount())
                .build();

        Question result = qnaRepository.save(question);
        return result.getQuestionId();
    }

    // Q&A 삭제
    @Override
    public void deleteQna(Integer questionId, String requesterId) {
        Question question = qnaRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 작성자 본인만 삭제 가능
        AuthUtil.checkOwnership(question.getUserId().getUserId(), requesterId);

        qnaRepository.delete(question);
    }

    // Q&A 작성자 Id 가져오기
    @Override
    public String findQnaId(Integer questionId) {
        Question question = qnaRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("게시글 아이디 데이터를 찾을 수 없습니다."));
        return question.getUserId().getUserId();
    }

    @Override
    public void editQna(QuestionDTO dto, String requesterId) {
        Optional<Question> result = qnaRepository.findById(dto.getQuestionId());

        Question question = result.orElseThrow();

        AuthUtil.checkOwnership(question.getUserId().getUserId(), requesterId);
        question.editTitle(dto.getTitle());
        question.editContent(dto.getContent());

        qnaRepository.save(question);
    }
}

