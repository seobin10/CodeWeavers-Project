package com.cw.cwu.service.admin;

import com.cw.cwu.domain.Notice;
import com.cw.cwu.domain.User;
import com.cw.cwu.dto.NoticeDTO;
import com.cw.cwu.repository.NoticeRepository;
import com.cw.cwu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminNoticeServiceImpl implements AdminNoticeService { // 공지 서비스

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // 공지사항 작성 메서드
    @Override
    public Integer writeNotice(NoticeDTO dto, String adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("해당하는 사용자 데이터를 찾을 수 없습니다."));
        Boolean isPinned = dto.getPin() == 1 ? true : false;
        Notice notice = Notice.builder()
                .noticeId(dto.getNoticeId())
                .adminId(admin)
                .title(dto.getTitle())
                .content(dto.getContent())
                .noticeDate(dto.getNoticeDate())
                .pin(isPinned)
                .viewCount(dto.getViewCount())
                .build();
        Notice result = noticeRepository.save(notice);
        return result.getNoticeId();
    }

    // 공지 리스트를 전부 가져오는 메서드
    @Override
    public List<NoticeDTO> findAllNotice() {
        return noticeRepository.findAllNotice()
                .stream()
                .map(row -> {
                    LocalDate noticeDate;
                    if (row[4] instanceof java.sql.Date) {
                        noticeDate = ((java.sql.Date) row[4]).toLocalDate();
                    } else if (row[4] instanceof String) {
                        noticeDate = LocalDate.parse((String) row[4]);
                    } else {
                        throw new RuntimeException("Unexpected type for noticeDate: " + row[4].getClass().getClass().getName());
                    }
                    Integer isPinned = ((Boolean) row[0]) ? 1 : 0;
                    NoticeDTO dto = new NoticeDTO(
                            (Integer) row[1],
                            (String) row[3],
                            (String) row[2],
                            (String) row[6],
                            noticeDate,
                            isPinned,
                            (Integer) row[5]
                    );
                    return dto;

                })
                .collect(Collectors.toList());
    }

    // 공지 게시물의 상세 정보를 가져오는 메서드
    public NoticeDTO getNoticeInfo(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("해당하는 공지 게시글이 없습니다."));
        return modelMapper.map(notice, NoticeDTO.class);
    }

    // 조회수 업데이트
    @Override
    public NoticeDTO updateViewCount(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("해당하는 데이터를 찾을 수 없습니다."));
        int changedCount = notice.getViewCount() + 1;
        notice.setViewCount(changedCount);
        noticeRepository.save(notice);
        Integer isPinned = (notice.isPin()) ? 1 : 0;
        NoticeDTO dto = new NoticeDTO();
        dto.setNoticeId(notice.getNoticeId());
        dto.setAdminId(String.valueOf(notice.getAdminId()));
        dto.setTitle(notice.getTitle());
        dto.setContent(notice.getContent());
        dto.setPin(isPinned);

        return dto;
    }

    // 게시글을 수정하는 메서드
    @Override
    public void updateNotice(Integer noticeId, NoticeDTO dto) {
        Optional<Notice> result = noticeRepository.findById(dto.getNoticeId());
        boolean isPinned = dto.getPin() == 1 ? true : false;
        Notice notice = result.orElseThrow();
        notice.editTitle(dto.getTitle());
        notice.editContent(dto.getContent());
        notice.editPin(isPinned);
        noticeRepository.save(notice);
    }


    // 게시글을 삭제하는 메서드
    @Override
    public void deleteNotice(Integer noticeId) {
        noticeRepository.deleteById(noticeId);
    }

}
