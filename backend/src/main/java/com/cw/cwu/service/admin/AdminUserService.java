package com.cw.cwu.service.admin;

import com.cw.cwu.dto.*;

import java.util.List;

public interface AdminUserService {
   String createUser(UserCreateRequestDTO dto);
   PageResponseDTO<UserDTO> getAllUsers(String keyword, PageRequestDTO pageRequestDTO);
   String deleteUser(String userId);

   String updateUser(UserUpdateRequestDTO dto);
   String resetPassword(String userId);
}