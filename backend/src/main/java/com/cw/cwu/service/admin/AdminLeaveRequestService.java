package com.cw.cwu.service.admin;

import com.cw.cwu.dto.LeaveRequestDTO;

import java.util.List;

public interface AdminLeaveRequestService {

    public void responseLeave(LeaveRequestDTO dto); //휴학 처리
    public List<LeaveRequestDTO> findAllLeaveData(); // 휴학 리스트 전체 조회
    public String findLeaveUserName(Integer leaveId);
}
