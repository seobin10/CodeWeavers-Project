package com.cw.cwu.controller.admin;

import com.cw.cwu.domain.ScheduleType;
import com.cw.cwu.dto.ScheduleRequestDTO;
import com.cw.cwu.dto.ScheduleResponseDTO;
import com.cw.cwu.dto.SemesterRequestDTO;
import com.cw.cwu.dto.SemesterResponseDTO;
import com.cw.cwu.service.admin.AdminScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admin/schedule")
@RequiredArgsConstructor
public class AdminScheduleController {

    private final AdminScheduleService adminScheduleService;

    // 학기 등록
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/semester")
    public ResponseEntity<String> createSemester(@RequestBody SemesterRequestDTO dto) {
        adminScheduleService.createSemester(dto);
        return ResponseEntity.ok("학기가 등록되었습니다.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/semester/{semesterId}")
    public ResponseEntity<String> updateSemester(
            @PathVariable("semesterId") Integer semesterId,
            @RequestBody SemesterRequestDTO dto) {
        adminScheduleService.updateSemester(semesterId, dto);
        return ResponseEntity.ok("학기가 수정되었습니다.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/semester/{semesterId}")
    public ResponseEntity<String> deleteSemester(@PathVariable("semesterId") Integer semesterId) {
        adminScheduleService.deleteSemester(semesterId);
        return ResponseEntity.ok("학기가 삭제되었습니다.");
    }


    //  학기 전체 조회
    @GetMapping("/semester")
    public ResponseEntity<List<SemesterResponseDTO>> getAllSemesters() {
        return ResponseEntity.ok(adminScheduleService.getAllSemesters());
    }

    //  일정 등록 또는 수정
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> setSchedule(@RequestBody ScheduleRequestDTO dto) {
        String result = adminScheduleService.setSchedule(dto);
        return ResponseEntity.ok(result);
    }

    // 특정 일정 조회
    @GetMapping("/{semesterId}/{type}")
    public ResponseEntity<ScheduleResponseDTO> getSchedule(
            @PathVariable("semesterId") Integer semesterId,
            @PathVariable("type") ScheduleType type) {

        ScheduleResponseDTO response = adminScheduleService.getSchedule(type, semesterId);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

}