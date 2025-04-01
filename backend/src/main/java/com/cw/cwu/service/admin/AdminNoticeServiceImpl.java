package com.cw.cwu.service.admin;
import com.cw.cwu.dto.NoticeDTO;
import com.cw.cwu.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminNoticeServiceImpl implements AdminNoticeService{

    private final NoticeRepository noticeRepository;

    // 공지사항을 탐색 메서드(미완)
    @Override
    public List<NoticeDTO> findAllNotice() {
        return List.of();
    }
}
