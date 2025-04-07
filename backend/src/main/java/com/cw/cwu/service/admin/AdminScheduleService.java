package com.cw.cwu.service.admin;

import com.cw.cwu.domain.ScheduleType;
import com.cw.cwu.dto.ScheduleRequestDTO;
import com.cw.cwu.dto.ScheduleResponseDTO;

public interface AdminScheduleService {

    /**
     * 학사 일정 저장 또는 수정
     */
    String setSchedule(ScheduleRequestDTO dto);

    /**
     * 특정 학사 일정 조회
     */
    ScheduleResponseDTO getSchedule(ScheduleType type);

    /**
     * 특정 일정이 현재 열려 있는지 확인 (기간 내인지)
     */
    boolean isScheduleOpen(ScheduleType type);
}
