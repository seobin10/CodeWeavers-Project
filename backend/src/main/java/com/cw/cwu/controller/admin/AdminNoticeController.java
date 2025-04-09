package com.cw.cwu.controller.admin;

import com.cw.cwu.dto.NoticeDTO;
import com.cw.cwu.service.admin.AdminNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/notice")
@RequiredArgsConstructor
public class AdminNoticeController {
    private final AdminNoticeService adminNoticeService;

    //공지사항 리스트 불러오기
    @GetMapping("/list")
    public ResponseEntity<List<NoticeDTO>> findNoticeList() {
        List<NoticeDTO> noticeList = adminNoticeService.findAllNotice();
        return ResponseEntity.ok(noticeList);
    }

    //공지사항 등록
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/write")
    public ResponseEntity<String> writeNotice(@RequestBody NoticeDTO dto, @RequestParam String adminId) {
        Integer noticeId = adminNoticeService.writeNotice(dto, adminId);
        return ResponseEntity.ok(noticeId + "번 게시글을 작성 완료했습니다.");
    }

    // 공지사항 내용 조회
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDTO> getNoticeInfo(@PathVariable("noticeId") Integer noticeId) {
        return ResponseEntity.ok(adminNoticeService.getNoticeInfo(noticeId));
    }

    //공지사항 조회수 처리
    @PutMapping("/update/{noticeId}")
    public ResponseEntity<String> updateCount(@PathVariable("noticeId") Integer noticeId) {
        adminNoticeService.updateViewCount(noticeId);
        return ResponseEntity.ok("조회수 업데이트가 완료되었습니다.");
    }

    //공지사항 수정
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/edit/{noticeId}")
    public ResponseEntity<String> editNotice(@PathVariable(name = "noticeId") Integer noticeId, @RequestBody NoticeDTO dto) {
        adminNoticeService.updateNotice(noticeId, dto);
        return ResponseEntity.ok(noticeId + "번 공지를 수정했습니다.");
    }

    //공지사항 삭제
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{noticeId}")
    public  ResponseEntity<String> clearNotice(@PathVariable(name = "noticeId") Integer noticeId) {
        adminNoticeService.deleteNotice(noticeId);
        return ResponseEntity.ok(noticeId + "번 공지를 삭제했습니다.");
    }

}
