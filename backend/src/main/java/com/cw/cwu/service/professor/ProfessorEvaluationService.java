package com.cw.cwu.service.professor;

import com.cw.cwu.dto.EvaluationAnswerDTO;
import com.cw.cwu.dto.EvaluationLectureDTO;

import java.util.List;

public interface ProfessorEvaluationService {
    public List<EvaluationLectureDTO> findAllLecture();
    public List<EvaluationAnswerDTO> findAnswersByEvaluationId(Integer evaluationId);
}
