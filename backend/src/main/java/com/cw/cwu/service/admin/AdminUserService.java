package com.cw.cwu.service.admin;

import com.cw.cwu.dto.UserCreateRequestDTO;

public interface AdminUserService {
    void createUser(UserCreateRequestDTO dto);
}