package com.cw.cwu.service;

import com.cw.cwu.dto.UserDTO;
import com.cw.cwu.domain.User;
import com.cw.cwu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    // 로그인
    public UserDTO login(UserDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        // 비밀번호 검증
        if (!user.getUserPassword().equals(request.getUserPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        return convertToDTO(user);
    }

    // 정보 조회
    public UserDTO getUserInfo(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        return convertToDTO(user);
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
            response.put("password", userPassword.get()); // ✅ JSON 형식으로 반환
        } else {
            response.put("error", "해당 학번과 이메일을 가진 사용자를 찾을 수 없습니다."); // ✅ JSON 에러 메시지 포함
        }

        return response;
    }




    // 이메일과 전화번호 업데이트
    public UserDTO updateUser(String userId, UserDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        user.setUserEmail(request.getUserEmail());
        user.setUserPhone(request.getUserPhone());

        userRepository.save(user);

        return convertToDTO(user);
    }

}
