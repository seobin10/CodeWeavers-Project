package com.cw.cwu.repository.user;

import com.cw.cwu.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnaRepository extends JpaRepository<Question, Integer> {
    // 전체 리스트를 조회하는 쿼리
    @Query(value = """
    SELECT q.question_id AS questionId, q.title, q.content, u.user_name AS userName, q.created_at AS createdAt, 
    q.status, q.view_count AS viewCount
    FROM questions q
    JOIN users u ON q.user_id = u.user_id
    """, nativeQuery = true)
    List<Object[]> findAllQna();

    // 특정 아이디의 게시글 및 답변을 조회하는 쿼리
    @Query(value = """
    SELECT q.question_id, q.view_count, q.title, u.user_name, q.created_at, q.content, a.content
    FROM questions q
    JOIN users u ON q.user_id = u.user_id
    LEFT JOIN answers a ON q.question_id = a.question_id
    WHERE q.question_id = :questionId
    """, nativeQuery = true)
    List<Object[]> findAnswer(@Param("questionId") Integer questionId);
}
