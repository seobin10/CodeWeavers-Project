package com.cw.cwu.repository.admin;

import com.cw.cwu.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {
// 공지 게시판 처리를 위한 레포지터리 //

// 공지 게시판 조회 쿼리
@Query(value = """
        SELECT is_pinned, notice_id, title, admin_id,
        created_at, view_count, content
        FROM notices
        ORDER BY 1 DESC;
        """, nativeQuery = true)
    List<Object[]> findAllNotice();
}
