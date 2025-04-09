package com.cw.cwu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO {
    // 공지사항 DTO
    private Integer noticeId;
    private String adminId;
    private String title;
    private String content;
    private LocalDate noticeDate;
    private Integer pin;
    private Integer viewCount;
}
