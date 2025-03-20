package com.cw.cwu.service;

import com.cw.cwu.dto.UserDTO;
import com.cw.cwu.domain.User;
import com.cw.cwu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

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

    @Override
    public String findUserIdByUserName(String username) {
        System.out.println("service user name 으로 userid(학번) 찾기 :  " + username);
        return userRepository.findUserIdByUserName(username).get();
    }

    // 이메일과 전화번호 업데이트 (ModelMapper 적용)
    public UserDTO updateUser(String userId, UserDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        modelMapper.map(request, user);

        userRepository.save(user);

        return modelMapper.map(user, UserDTO.class);
    }
}

