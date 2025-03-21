package com.cw.cwu.service.user;

import com.cw.cwu.domain.Question;
import com.cw.cwu.domain.Status;
import com.cw.cwu.dto.AnswerDTO;
import com.cw.cwu.dto.QuestionDTO;
import com.cw.cwu.dto.UserDTO;
import com.cw.cwu.domain.User;
import com.cw.cwu.repository.user.QnaRepository;
import com.cw.cwu.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final QnaRepository qnaRepository;
    // 로그인
    public UserDTO login(UserDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        // 비밀번호 검증
        if (!user.getUserPassword().equals(request.getUserPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        return modelMapper.map(user, UserDTO.class);
    }

    // 정보 조회
    public UserDTO getUserInfo(String userId) {
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

    // pw 찾기
    public Map<String, String> findUserPasswordByUserIdAndEmail(String userId, String userEmail) {
        System.out.println("service user id와 이메일로 userpassword(비밀번호) 찾기 : " + userId + ", " + userEmail);

        Map<String, String> response = new HashMap<>();
        Optional<String> userPassword = userRepository.findUserPasswordByUserIdAndEmail(userId, userEmail);

        if (userPassword.isPresent()) {
            response.put("password", userPassword.get());
        } else {
            response.put("error", "해당 학번과 이메일을 가진 사용자를 찾을 수 없습니다.");
        }

        return response;
    }

    // 이메일과 전화번호 업데이트 (ModelMapper 적용)
    public UserDTO updateUser(String userId, UserDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        modelMapper.getConfiguration().setPropertyCondition(context -> context.getSource() != null);
        modelMapper.map(request, user);

        userRepository.save(user);

        return modelMapper.map(user, UserDTO.class);
    }

    // QnA 게시판 리스트를 조회
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
    public List<AnswerDTO> findAnswer(Integer questionId) {
        return qnaRepository.findAnswer(questionId)
                .stream()
                .map(row -> new AnswerDTO(
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
    public Integer writeQna(@RequestBody QuestionDTO dto) {
        String userId = findUserIdByUserName(dto.getUserName());

        User user = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("userId 데이터를 찾을 수 없습니다."));
        Question question = Question.builder()
                .questionId(dto.getQuestionId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .userId(user)
                .questionDate(dto.getCreatedAt())
                .status(dto.statusToEnum(dto.getStatus()))
                .viewCount(dto.getViewCount()).build();
        Question result = qnaRepository.save(question);
        return result.getQuestionId();
    }
}



