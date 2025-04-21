package com.cw.cwu.controller.admin;

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
}