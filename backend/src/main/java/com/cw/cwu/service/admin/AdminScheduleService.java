package com.cw.cwu.service.admin;

import com.cw.cwu.domain.ScheduleType;
import com.cw.cwu.dto.ScheduleRequestDTO;
import com.cw.cwu.dto.ScheduleResponseDTO;
import com.cw.cwu.dto.SemesterRequestDTO;
import com.cw.cwu.dto.SemesterResponseDTO;

import java.util.List;
import java.util.Optional;

public interface AdminScheduleService {

    String setSchedule(ScheduleRequestDTO dto);

    ScheduleResponseDTO getSchedule(ScheduleType type, Integer semesterId);

    void updateSemester(Integer semesterId, SemesterRequestDTO dto);

    void deleteSemester(Integer semesterId);


    void createSemester(SemesterRequestDTO dto);

    List<SemesterResponseDTO> getAllSemesters();

    boolean isScheduleOpen(ScheduleType scheduleType);

    Integer getCurrentSemesterId();

    // 수강신청 학기 찾기
    Integer getEnrollSemesterId();

    Integer getSemesterIdByScheduleType(ScheduleType type);

    Optional<Integer> getUpcomingSemesterId();
}