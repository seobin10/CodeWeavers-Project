package com.cw.cwu.service.admin;

import com.cw.cwu.dto.NoticeDTO;

import java.util.List;

public interface AdminNoticeService {
    public List<NoticeDTO> findAllNotice();
}
