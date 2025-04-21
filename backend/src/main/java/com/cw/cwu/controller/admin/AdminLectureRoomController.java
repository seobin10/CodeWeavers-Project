package com.cw.cwu.controller.admin;

import com.cw.cwu.domain.RoomStatus;
import com.cw.cwu.dto.BuildingDTO;
import com.cw.cwu.dto.LectureRoomStatusDTO;
import com.cw.cwu.dto.LectureRoomUsageDTO;
import com.cw.cwu.service.admin.AdminLectureRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/lecture-rooms")
@RequiredArgsConstructor
public class AdminLectureRoomController {

    private final AdminLectureRoomService adminLectureRoomService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{roomId}/usage")
    public ResponseEntity<List<LectureRoomUsageDTO>> getLectureRoomUsage(@PathVariable Integer roomId) {
        List<LectureRoomUsageDTO> result = adminLectureRoomService.getCurrentClassesByLectureRoom(roomId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/buildings/{buildingId}/lecture-rooms")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LectureRoomStatusDTO>> getLectureRoomsByBuilding(@PathVariable Integer buildingId) {
        List<LectureRoomStatusDTO> result = adminLectureRoomService.getRoomsByBuilding(buildingId);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/buildings")
    public ResponseEntity<List<BuildingDTO>> getAllBuildings() {
        List<BuildingDTO> result = adminLectureRoomService.getAllBuildings();
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createLectureRoom(
            @RequestParam String roomName,
            @RequestParam Integer buildingId
    ) {
        adminLectureRoomService.createLectureRoom(roomName, buildingId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{roomId}")
    public ResponseEntity<Void> updateLectureRoom(
            @PathVariable Integer roomId,
            @RequestParam(required = false) String newName,
            @RequestParam(required = false) String newStatus
    ) {
        adminLectureRoomService.updateLectureRoom(roomId, newName,
                newStatus != null ? RoomStatus.valueOf(newStatus) : null);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteLectureRoom(@PathVariable Integer roomId) {
        adminLectureRoomService.deleteLectureRoom(roomId);
        return ResponseEntity.ok().build();
    }
}