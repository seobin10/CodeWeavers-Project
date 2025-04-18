package com.cw.cwu.controller.professor;

import com.cw.cwu.dto.EvaluationAnswerDTO;
import com.cw.cwu.dto.EvaluationLectureDTO;
import com.cw.cwu.service.professor.ProfessorEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/professor/evaluation")
@RequiredArgsConstructor
public class ProfessorEvaluationController {

    private final ProfessorEvaluationService service;
    // 강의 평가 리스트 모두 가져오기
    @GetMapping("/list")
    public ResponseEntity<List<EvaluationLectureDTO>> readLectureList() {
        List<EvaluationLectureDTO> lectureList = service.findAllLecture();
        return ResponseEntity.ok(lectureList);
    }

    // 강의 평가 내용 찾기
    @GetMapping("/answer")
    public ResponseEntity<List<EvaluationAnswerDTO>> getAnswersByEvaluationId(
            @RequestParam("evaluationId") Integer evaluationId
    ) {
        List<EvaluationAnswerDTO> answers = service.findAnswersByEvaluationId(evaluationId);
        return ResponseEntity.ok(answers);
    }


}
