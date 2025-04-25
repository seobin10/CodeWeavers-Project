package com.cw.cwu.controller.student;

import com.cw.cwu.dto.LeaveRequestDTO;
import com.cw.cwu.dto.ReturnRequestDTO;
import com.cw.cwu.service.student.StudentLeaveRequestService;
import com.cw.cwu.service.student.StudentReturnRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students/request")
@RequiredArgsConstructor
public class StudentRequestController {
    private final StudentLeaveRequestService leaveService;
    private final StudentReturnRequestService returnService;

    // 휴학 신청
    @PostMapping("/leave")
    public String createRequestLeave(@RequestBody LeaveRequestDTO dto, @RequestParam("studentId") String studentId){
        Integer leaveId = leaveService.requestLeave(dto, studentId);
        return "createRequest : No." + leaveId;
    }

    // 휴학 내역 조회 -자기자신의 신청 내역을 조회합니다
    @GetMapping("/leave/list")
    public ResponseEntity<List<LeaveRequestDTO>> seeMyRequest(@RequestParam("studentId") String studentId) {
        List<LeaveRequestDTO> requestList = leaveService.findLeaveData(studentId);
        return ResponseEntity.ok(requestList);
    }

    // 복학 등록
    @PostMapping("/return")
    public String createRequestReturn(@RequestBody ReturnRequestDTO dto, @RequestParam("studentId") String studentId){
        System.out.println(seeMyRequest(studentId).getBody());
        boolean isAprroved = seeMyRequest(studentId).getBody().stream().anyMatch(d ->
                "APPROVED".equals(d.getStatus())||"승인".equals(d.getStatus()));
        // 휴학 데이터가 있고,isAprroved가 참인 경우에만, 복학 데이터를 저장하도록 한다.
        if(seeMyRequest(studentId).getBody().isEmpty() || !isAprroved){
            return "휴학한 사람만 복학 신청을 할 수 있습니다!";
        }
        Integer returnId = returnService.requestReturn(dto, studentId);
        return "createRequest : No." + returnId;
    }

    // 복학 조회 - 자기자신의 복학 내역을 조회합니다
    @GetMapping("/return/list")
    public ResponseEntity<List<ReturnRequestDTO>> seeMyReturnRequest(@RequestParam("studentId") String studentId) {
        List<ReturnRequestDTO> requestList = returnService.findReturnData(studentId);
        return ResponseEntity.ok(requestList);
    }
}
