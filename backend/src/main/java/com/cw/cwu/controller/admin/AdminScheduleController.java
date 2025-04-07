package com.cw.cwu.controller.admin;

import com.cw.cwu.domain.ScheduleType;
import com.cw.cwu.dto.ScheduleRequestDTO;
import com.cw.cwu.dto.ScheduleResponseDTO;
import com.cw.cwu.service.admin.AdminScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/schedule")
@RequiredArgsConstructor
public class AdminScheduleController {

    private final AdminScheduleService adminScheduleService;

    // 기간 등록 또는 수정
    @PostMapping
    public ResponseEntity<String> setSchedule(@RequestBody ScheduleRequestDTO dto) {
        String result = adminScheduleService.setSchedule(dto);
        return ResponseEntity.ok(result);
    }

    // 특정 일정 조회
    @GetMapping("/{type}")
    public ResponseEntity<ScheduleResponseDTO> getSchedule(@PathVariable("type") ScheduleType type) {
        ScheduleResponseDTO response = adminScheduleService.getSchedule(type);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    // 특정 일정 오픈 여부 확인
    @GetMapping("/{type}/is-open")
    public ResponseEntity<Boolean> isScheduleOpen(@PathVariable("type") ScheduleType type) {
        boolean result = adminScheduleService.isScheduleOpen(type);
        return ResponseEntity.ok(result);
    }
}