package com.cw.cwu.service.admin;

import com.cw.cwu.dto.PageRequestDTO;
import com.cw.cwu.dto.PageResponseDTO;
import com.cw.cwu.dto.UserCreateRequestDTO;
import com.cw.cwu.dto.UserDTO;

import java.util.List;

public interface AdminUserService {
   String createUser(UserCreateRequestDTO dto);
   PageResponseDTO<UserDTO> getAllUsers(String keyword, PageRequestDTO pageRequestDTO);
   String deleteUser(String userId);
}