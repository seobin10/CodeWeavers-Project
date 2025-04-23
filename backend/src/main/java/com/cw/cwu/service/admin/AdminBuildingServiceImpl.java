package com.cw.cwu.service.admin;

import com.cw.cwu.domain.*;
import com.cw.cwu.dto.BuildingDTO;
import com.cw.cwu.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminBuildingServiceImpl implements AdminBuildingService {

    private final BuildingRepository buildingRepository;
    private final LectureRoomRepository lectureRoomRepository;
    private final ClassEntityRepository classEntityRepository;
    private final AdminLectureRoomService adminLectureRoomService;

    // 건물 등록
    @Override
    public void createBuilding(String name, BuildingStatus status) {
        boolean exists = buildingRepository.existsByName(name);
        if (exists) {
            throw new IllegalArgumentException("이미 동일한 이름의 건물이 존재합니다.");
        }

        Building building = Building.builder()
                .name(name)
                .status(status)
                .build();

        buildingRepository.save(building);
    }

    // 건물 전체 조회
    @Override
    public List<BuildingDTO> getAllBuildings() {
        return buildingRepository.findAll().stream()
                .sorted((b1, b2) -> {
                    int s1 = b1.getStatus() == BuildingStatus.UNAVAILABLE ? 1 : 0;
                    int s2 = b2.getStatus() == BuildingStatus.UNAVAILABLE ? 1 : 0;
                    return Integer.compare(s1, s2);
                })
                .map(b -> BuildingDTO.builder()
                        .buildingId(b.getId())
                        .buildingName(b.getName())
                        .status(b.getStatus().name())
                        .build())
                .collect(Collectors.toList());
    }

    // 건물 수정
    @Override
    public void updateBuilding(Integer buildingId, String newName, BuildingStatus newStatus) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("해당 건물을 찾을 수 없습니다."));

        // 상태를 비활성화 하려는 경우 바꾸려는 경우 강의실 중 사용 중인 게 있는지 확인
        if (newStatus == BuildingStatus.UNAVAILABLE) {
            Integer usableSemesterId = adminLectureRoomService.resolveUsableSemesterId();
            if (usableSemesterId != null) {
                List<LectureRoom> lectureRooms = lectureRoomRepository.findByBuilding_Id(buildingId);
                boolean hasAvailableRoomInUse = lectureRooms.stream().anyMatch(
                        room -> classEntityRepository.existsByLectureRoom_IdAndSemester_Id(room.getId(), usableSemesterId)
                );
                if (hasAvailableRoomInUse) {
                    throw new IllegalStateException("사용 중인 강의실이 있어 요청을 실행할 수 없습니다.");
                }
            }
        }

        if (newName != null && !newName.trim().isEmpty() && !newName.equals(building.getName())) {
            boolean exists = buildingRepository.existsByNameAndIdNot(newName, buildingId);
            if (exists) {
                throw new IllegalArgumentException("이미 동일한 이름의 건물이 존재합니다.");
            }
            building.setName(newName);
        }

        if (newStatus != null) {
            building.setStatus(newStatus);

            List<LectureRoom> rooms = lectureRoomRepository.findByBuilding_Id(buildingId);

            if (newStatus == BuildingStatus.UNAVAILABLE) {
                rooms.forEach(room -> room.setStatus(LectureRoomStatus.UNAVAILABLE));
            } else if (newStatus == BuildingStatus.AVAILABLE) {
                rooms.forEach(room -> room.setStatus(LectureRoomStatus.AVAILABLE));
            }

            lectureRoomRepository.saveAll(rooms);
        }

        buildingRepository.save(building);
    }

    // 건물 삭제
    @Override
    public void deleteBuilding(Integer buildingId) {
        List<LectureRoom> lectureRooms = lectureRoomRepository.findByBuilding_Id(buildingId);

        // 수업 이력이 있는 강의실이 하나라도 있으면 삭제 불가
        boolean hasReferencedRoom = lectureRooms.stream()
                .anyMatch(room -> classEntityRepository.existsByLectureRoom_Id(room.getId()));

        if (hasReferencedRoom) {
            throw new IllegalStateException("해당 건물에는 수업 이력이 있는 강의실이 존재하므로 삭제할 수 없습니다.");
        }

        // 수업 이력이 없는 강의실만 있으면 강의실 먼저 삭제 후 건물 삭제
        lectureRoomRepository.deleteAll(lectureRooms);
        buildingRepository.deleteById(buildingId);
    }
}