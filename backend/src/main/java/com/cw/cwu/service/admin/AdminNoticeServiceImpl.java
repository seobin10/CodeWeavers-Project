package com.cw.cwu.service.admin;
import com.cw.cwu.dto.NoticeDTO;
import com.cw.cwu.repository.admin.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
