package com.cw.cwu.service.admin;

import com.cw.cwu.dto.AnswerDTO;

public interface AdminAnsService {
    public Integer writeAns(AnswerDTO dto, String userId);
    public void deleteAns(Integer questionId);
}
