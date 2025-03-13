package com.green.codeweavers.academymanager.service;

import com.green.codeweavers.academymanager.dto.UserDTO;
import org.springframework.data.jpa.repository.Query;

// 서비스에 대한 설계도
public interface UserService {
    UserDTO see(String userId); // 조회

}
