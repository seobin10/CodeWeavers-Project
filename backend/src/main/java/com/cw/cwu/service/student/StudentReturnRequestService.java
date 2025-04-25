package com.cw.cwu.service.student;

import com.cw.cwu.dto.ReturnRequestDTO;

import java.util.List;

public interface StudentReturnRequestService{
    public Integer requestReturn(ReturnRequestDTO dto, String studentId); // 복학 신청
    public List<ReturnRequestDTO> findReturnData(String studentId); // 내 복학 조회
}
