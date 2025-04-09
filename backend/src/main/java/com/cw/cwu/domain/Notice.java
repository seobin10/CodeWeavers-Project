package com.cw.cwu.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "notices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice { // 공지사항 엔터티
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Integer noticeId;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User adminId;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private LocalDate noticeDate;

    @Column(name = "is_pinned")
    private boolean pin;

    @Column(name= "view_count")
    private Integer viewCount;

    public void editTitle(String title){this.title=title;}
    public void editContent(String content){this.content=content;}
    public void editPin(boolean pin){this.pin=pin;}
}
