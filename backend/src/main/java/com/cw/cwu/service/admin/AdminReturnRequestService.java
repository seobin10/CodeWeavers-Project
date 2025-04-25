package com.cw.cwu.service.admin;

import com.cw.cwu.dto.ReturnRequestDTO;

import java.util.List;

public interface AdminReturnRequestService {
    public void responseReturn(ReturnRequestDTO dto); // 복학 신청 처리 메서드
    public List<ReturnRequestDTO> findAllReturnData(); // 복학 전체 조회 메서드
    public String findReturnUserName(Integer returnId);
}
