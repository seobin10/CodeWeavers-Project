package com.cw.cwu.service.student;

import com.cw.cwu.domain.EvaluationLecture;
import com.cw.cwu.dto.EvaluationAnswerDTO;
import com.cw.cwu.dto.EvaluationCourseDTO;
import com.cw.cwu.dto.EvaluationLectureDTO;
import com.cw.cwu.dto.EvaluationQuestionDTO;

import java.util.List;

public interface StudentEvaluationService {
    public List<EvaluationQuestionDTO> findAllList();
    public List<EvaluationCourseDTO> getCurrentSemesterCourses(String studentId);
    public Integer saveEvaluationAnswer(String userId, Integer questionId, Integer classId, EvaluationLectureDTO lectDto, EvaluationAnswerDTO dto);
    public EvaluationLecture saveEvaluationLog(EvaluationLectureDTO dto, String userId, Integer classId);
    public List<EvaluationLectureDTO> findEvaluationStatus(String studentId);
}
