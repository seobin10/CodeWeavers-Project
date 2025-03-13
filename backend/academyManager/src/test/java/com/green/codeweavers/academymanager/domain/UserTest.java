package com.green.codeweavers.academymanager.domain;

import com.green.codeweavers.academymanager.dto.UserDTO;
import com.green.codeweavers.academymanager.repository.UserRepository;
import com.green.codeweavers.academymanager.service.UserService;
import groovy.util.logging.Log4j2;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
@Transactional
class UserTest {
    private static final Logger log = LogManager.getLogger(UserTest.class);
    @Autowired
    private UserRepository userRepository;


    @Test
    public void repoTest(){
    String stuNum = "202400001";
    java.util.Optional<User> res = userRepository.findById(stuNum);
    User user = res.orElseThrow();
    log.info("모든 데이터 [" + user + "]");
    log.info("학번 - " + user.getUserId());
    log.info("이름 - " + user.getUserName());
    log.info("학과 - " + user.getDepartment());
    }

    @Autowired
    private UserService userService;
    @Test
    public void dtoReadTest(){
        String userId = "202400002";
        UserDTO userDTO = userService.see(userId);
        log.info(userDTO);
    }
}