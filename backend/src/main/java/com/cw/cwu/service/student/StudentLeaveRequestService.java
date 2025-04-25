package com.cw.cwu.service.student;

import com.cw.cwu.dto.LeaveRequestDTO;

import java.util.List;

public interface StudentLeaveRequestService {
    public Integer requestLeave(LeaveRequestDTO dto, String studentId); // 휴학 신청
    public List<LeaveRequestDTO> findLeaveData(String studentId); // 내 휴학 신청 내역 조회
}
