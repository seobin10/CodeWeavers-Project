package com.cw.cwu.service.admin;

import com.cw.cwu.dto.LectureRoomUsageDTO;

import java.util.List;

public interface AdminLectureRoomService {
    List<LectureRoomUsageDTO> getCurrentClassesByLectureRoom(Integer roomId);
}
