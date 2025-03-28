package com.cw.cwu.repository.user;

import com.cw.cwu.domain.Answer;
import com.cw.cwu.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    // 질문 아이디 탐색
    Optional<Answer> findByQuestionId(Question question);

}
