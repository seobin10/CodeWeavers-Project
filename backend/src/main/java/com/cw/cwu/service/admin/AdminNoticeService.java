package com.cw.cwu.service.admin;

import com.cw.cwu.dto.NoticeDTO;

import java.util.List;

public interface AdminNoticeService {
    // 공지 리스트를 전부 가져오는 메서드
    public List<NoticeDTO> findAllNotice();

    // 게시글을 작성하는 메서드
    public Integer writeNotice(NoticeDTO dto, String adminId);

    // 게시글의 내용을 가져오는 메서드
    public NoticeDTO getNoticeInfo(Integer noticeId);

    // 조회수 업데이트 메서드
    public NoticeDTO updateViewCount(Integer noticeId);

    // 게시글을 수정하는 메서드
    public void updateNotice(Integer noticeId, NoticeDTO dto);

    // 게시글을 삭제하는 메서드
    public void deleteNotice(Integer noticeId);

}
