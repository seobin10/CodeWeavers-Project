package com.green.codeweavers.academymanager.service;

import com.green.codeweavers.academymanager.domain.User;
import com.green.codeweavers.academymanager.dto.UserDTO;
import com.green.codeweavers.academymanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

// 서비스에 대한 실제 메서드 구현
@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    //조회 서비스
    @Override
    public UserDTO see(String userId) {
        Optional<User> result = userRepository.findById(userId);
        User user = result.orElseThrow();
        UserDTO dto = modelMapper.map(user, UserDTO.class);
        return dto;
    }
}
