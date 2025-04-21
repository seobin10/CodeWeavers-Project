package com.cw.cwu.service.admin;

import com.cw.cwu.domain.*;
import com.cw.cwu.dto.BuildingDTO;
import com.cw.cwu.dto.LectureRoomStatusDTO;
import com.cw.cwu.dto.LectureRoomUsageDTO;
import com.cw.cwu.repository.BuildingRepository;
import com.cw.cwu.repository.ClassEntityRepository;
import com.cw.cwu.repository.LectureRoomRepository;
import com.cw.cwu.service.user.UserSemesterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminLectureRoomServiceImpl implements AdminLectureRoomService {

    private final ClassEntityRepository classEntityRepository;
    private final UserSemesterService userSemesterService;
    private final LectureRoomRepository lectureRoomRepository;
    private final BuildingRepository buildingRepository;
    private final AdminScheduleService adminScheduleService;

    /**
     * - 현재 날짜에 해당하는 학기가 존재하면 해당 학기를 우선 사용
     * - 현재 학기가 없고, 강의 등록 일정이 열려 있으면 등록 대상 학기를 사용
     * - 아직 시작되지 않은 "다음 학기"가 존재한다면 그 학기 사용
     * - 다 없을 경우 null 반환, 강의실 사용 없음으로 처리
     */
    private Integer resolveUsableSemesterId() {
        try {
            return userSemesterService.getCurrentSemester().getId();
        } catch (Exception e1) {
            try {
                return adminScheduleService.getSemesterIdByScheduleType(ScheduleType.CLASS);
            } catch (Exception e2) {
                return adminScheduleService.getUpcomingSemesterId().orElse(null);
            }
        }
    }

    // 강의실별 수업 조회
    @Override
    public List<LectureRoomUsageDTO> getCurrentClassesByLectureRoom(Integer roomId) {
        Integer semesterId = resolveUsableSemesterId();
        if (semesterId == null) return Collections.emptyList();

        List<ClassEntity> classes = classEntityRepository.findByLectureRoom_IdAndSemester_Id(roomId, semesterId);

        return classes.stream()
                .map(c -> LectureRoomUsageDTO.builder()
                        .classId(c.getId())
                        .courseName(c.getCourse().getName())
                        .professorName(c.getProfessor() != null ? c.getProfessor().getUserName() : "(미정)")
                        .departmentName(
                                c.getProfessor() != null && c.getProfessor().getDepartment() != null
                                        ? c.getProfessor().getDepartment().getDepartmentName()
                                        : "(미정)"
                        )
                        .day(c.getDay())
                        .startTime(c.getStartTime())
                        .endTime(c.getEndTime())
                        .lectureRoomName(c.getLectureRoom().getName())
                        .buildingName(c.getLectureRoom().getBuilding().getName())
                        .build()
                ).toList();
    }

    // 건물별 강의실 조회
    @Override
    public List<LectureRoomStatusDTO> getRoomsByBuilding(Integer buildingId) {
        List<LectureRoom> rooms = lectureRoomRepository.findByBuilding_Id(buildingId);
        return rooms.stream()
                .map(r -> LectureRoomStatusDTO.builder()
                        .roomId(r.getId())
                        .roomName(r.getName())
                        .status(r.getStatus().name())
                        .buildingName(r.getBuilding().getName())
                        .build()
                ).collect(Collectors.toList());
    }

    // 모든 건물 조회
    @Override
    public List<BuildingDTO> getAllBuildings() {
        return buildingRepository.findAll().stream()
                .map(b -> BuildingDTO.builder()
                        .buildingId(b.getId())
                        .buildingName(b.getName())
                        .status(b.getStatus().name()) // ENUM → 문자열
                        .build())
                .collect(Collectors.toList());
    }

    // 강의실 생성
    @Override
    public void createLectureRoom(String roomName, Integer buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("해당 건물을 찾을 수 없습니다."));

        boolean exists = lectureRoomRepository.existsByBuilding_IdAndName(buildingId, roomName);
        if (exists) {
            throw new IllegalArgumentException("이미 동일한 건물에 동일한 이름의 강의실이 존재합니다.");
        }

        LectureRoom room = LectureRoom.builder()
                .name(roomName)
                .status(RoomStatus.AVAILABLE)
                .building(building)
                .build();

        lectureRoomRepository.save(room);
    }

    // 강의실 정보 수정 (이름 + 상태)
    @Override
    public void updateLectureRoom(Integer roomId, String newName, RoomStatus newStatus) {
        LectureRoom room = lectureRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강의실을 찾을 수 없습니다."));

        if (newName != null && !newName.trim().isEmpty()) {
            // 동일 건물 내, 다른 roomId와 이름이 겹치는지 확인
            boolean nameExists = lectureRoomRepository.existsByBuilding_IdAndNameAndIdNot(
                    room.getBuilding().getId(), newName, roomId);

            if (nameExists) {
                throw new IllegalArgumentException("동일한 건물에 동일한 이름의 강의실이 이미 존재합니다.");
            }

            room.setName(newName);
        }

        if (newStatus != null) {
            room.setStatus(newStatus);
        }

        lectureRoomRepository.save(room);
    }

    // 강의실 삭제 - 참조된 이력이 있으면 삭제 불가
    @Override
    public void deleteLectureRoom(Integer roomId) {
        boolean isReferenced = classEntityRepository.existsByLectureRoom_Id(roomId);
        if (isReferenced) {
            throw new IllegalStateException("해당 강의실은 강의 기록이 존재하여 삭제할 수 없습니다.");
        }
        lectureRoomRepository.deleteById(roomId);
    }
}
