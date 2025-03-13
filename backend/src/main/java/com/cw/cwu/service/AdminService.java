package com.cw.cwu.service;

import com.cw.cwu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

}
