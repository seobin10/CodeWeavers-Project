package com.cw.cwu.controller.admin;

import com.cw.cwu.dto.LeaveRequestDTO;
import com.cw.cwu.dto.ReturnRequestDTO;
import com.cw.cwu.service.admin.AdminLeaveRequestService;
import com.cw.cwu.service.admin.AdminReturnRequestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/response")
@RequiredArgsConstructor
public class AdminResponseController {
    private final AdminLeaveRequestService leaveService;
    private final AdminReturnRequestService returnService;

    @GetMapping("/find/username")
    public ResponseEntity<String> getUserName(
            @RequestParam("type") String type,
            @RequestParam("id") Integer id) {
        String userName;
        switch (type.toLowerCase()) {
            case "leave":
                userName = leaveService.findLeaveUserName(id);
                break;
            case "return":
                userName = returnService.findReturnUserName(id);
                break;
            default:
                throw new IllegalArgumentException("지원하지 않는 type입니다: " + type);
        }
        return ResponseEntity.ok(userName);
    }

    // 휴학 신청 처리 - 학생의 휴학을 수락 혹은 거절합니다.
    @PutMapping("/leave/{leaveId}")
    public Map<String, String> allowRequestLeave(
            @PathVariable(name = "leaveId") Integer leaveId,
            @RequestBody LeaveRequestDTO dto,
            HttpServletRequest request) {
        dto.setLeaveId(leaveId);
        leaveService.responseLeave(dto);
        return Map.of("allowRequestLeave 수행 결과", "학생 요청에 성공적으로 응답했습니다.");
    }

    // 휴학 내역 전체 보기
    @GetMapping("/leave/list")
    public ResponseEntity<List<LeaveRequestDTO>> seeAllRequest() {
        List<LeaveRequestDTO> requestList = leaveService.findAllLeaveData();
        return ResponseEntity.ok(requestList);
    }

    // 복학 신청 처리 - 학생의 복학을 수락 혹은 거절합니다.
    @PutMapping("/return/{returnId}")
    public Map<String, String> allowRequestReturn(
            @PathVariable(name = "returnId") Integer returnId,
            @RequestBody ReturnRequestDTO dto,
            HttpServletRequest request) {
        dto.setReturnId(returnId);
        returnService.responseReturn(dto);
        return Map.of("allowRequestReturn 수행 결과", "학생 요청에 성공적으로 응답했습니다.");
    }

    // 복학 내역 전체 보기
    @GetMapping("/return/list")
    public ResponseEntity<List<ReturnRequestDTO>> seeAllReturnRequest() {
        List<ReturnRequestDTO> requestList = returnService.findAllReturnData();
        return ResponseEntity.ok(requestList);
    }
}
