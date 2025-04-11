package com.cw.cwu.controller.student;

import com.cw.cwu.domain.EvaluationQuestion;
import com.cw.cwu.dto.EvaluationQuestionDTO;
import com.cw.cwu.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students/evaluation")
@RequiredArgsConstructor
public class StudentEvaluationController {

    //dto 채우고, 메서드 만드세요
    // 질문 리스트 불러오기
    @GetMapping("/quelist")
    public ResponseEntity<List<EvaluationQuestionDTO>> findQueList(@RequestBody EvaluationQuestionDTO dto, @RequestParam Integer question_id){
        Integer questionId = EvaluationService.findAllList(dto, question_id);
        return ResponseEntity.ok(null);
    }
}
