package com.cw.cwu.service.user;

import com.cw.cwu.domain.User;
import com.cw.cwu.dto.QnADTO;
import com.cw.cwu.dto.QuestionDTO;
import com.cw.cwu.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public UserDTO login(UserDTO request);
    public UserDTO getUserInfo(String userId);
    Optional<User> findByUserId(String email);
    public String findUserIdByUserName(String username);
    //Qna
    public List<QuestionDTO> findAllQna();
    public List<QnADTO> findAnswer(Integer questionId);
    public QuestionDTO updateCount(Integer questionId);
    public Integer writeQna(QuestionDTO dto, String userId);
    public void deleteQna(Integer questionId);
    public String findQnaId(Integer questionId);
    public void editQna(QuestionDTO dto);

    // User 엔티티 -> UserDTO 변환
    default UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setUserPassword(user.getUserPassword());
        dto.setUserBirth(user.getUserBirth());
        dto.setUserEmail(user.getUserEmail());
        dto.setUserPhone(user.getUserPhone());
        dto.setUserRole(user.getUserRole().toString());
        dto.setUserImgUrl(user.getUserImgUrl());
        dto.setDepartmentId(user.getDepartment() != null ? user.getDepartment().getDepartmentId() : null);
        dto.setDepartmentName(user.getDepartment() != null ? user.getDepartment().getDepartmentName() : null);
        return dto;
    }

}
