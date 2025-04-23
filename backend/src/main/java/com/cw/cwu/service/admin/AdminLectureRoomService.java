package com.cw.cwu.service.admin;

import com.cw.cwu.domain.LectureRoomStatus;
import com.cw.cwu.dto.BuildingDTO;
import com.cw.cwu.dto.LectureRoomStatusDTO;
import com.cw.cwu.dto.LectureRoomUsageDTO;

import java.util.List;

public interface AdminLectureRoomService {
    Integer resolveUsableSemesterId();

    List<LectureRoomUsageDTO> getCurrentClassesByLectureRoom(Integer roomId);

    List<LectureRoomStatusDTO> getRoomsByBuilding(Integer buildingId);

    List<BuildingDTO> getAllBuildings();

    // 강의실 생성
    void createLectureRoom(String roomName, Integer buildingId);

    // 강의실 정보 수정 (이름 + 상태)
    void updateLectureRoom(Integer roomId, String newName, LectureRoomStatus newStatus);

    // 강의실 삭제 - 참조된 이력이 있으면 삭제 불가
    void deleteLectureRoom(Integer roomId);
}
