package com.cw.cwu.service.user;

import com.cw.cwu.domain.User;
import com.cw.cwu.dto.QnADTO;
import com.cw.cwu.dto.QuestionDTO;
import com.cw.cwu.dto.UserDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    public UserDTO login(UserDTO request);
    public Optional<User> findByUserId(String userId);

    // 정보 조회
    UserDTO getUserInfo(String userId, String requesterId);
    public Map<String, String> findUserPasswordByUserIdAndEmail(String userId, String userEmail);
    public String findUserIdByUserName(String username);
    void savePasswordResetToken(User user, String token);
    void sendResetEmail(String toEmail, String resetLink);
    void save(User user);
    // 이메일과 전화번호 업데이트
    UserDTO updateUser(String userId, UserDTO request, String requesterId);

    //Qna
    public List<QuestionDTO> findAllQna();
    public List<QnADTO> findAnswer(Integer questionId);
    public QuestionDTO updateCount(Integer questionId);
    public Integer writeQna(QuestionDTO dto, String userId);
    void deleteQna(Integer questionId, String requesterId);

    public String findQnaId(Integer questionId);
    void editQna(QuestionDTO dto, String requesterId);

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
