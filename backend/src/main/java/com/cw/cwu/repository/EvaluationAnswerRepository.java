package com.cw.cwu.repository;

import com.cw.cwu.domain.EvaluationAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluationAnswerRepository extends JpaRepository<EvaluationAnswer, Integer> {
    @Query(value = """
            SELECT 
                answer_id, evaluation_id, question_id, answer_choice, subjective_text
            FROM evaluation_answers
            WHERE evaluation_id = :evaluation_id
            """, nativeQuery = true)
    List<Object[]> findAnswers(@Param("evaluation_id") Integer evaluationId);

}
