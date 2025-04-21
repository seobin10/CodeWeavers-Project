package com.cw.cwu.controller.student;

import com.cw.cwu.dto.EvaluationAnswerDTO;
import com.cw.cwu.dto.EvaluationCourseDTO;
import com.cw.cwu.dto.EvaluationLectureDTO;
import com.cw.cwu.dto.EvaluationQuestionDTO;
import com.cw.cwu.service.student.StudentEvaluationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students/evaluation")
@RequiredArgsConstructor
public class StudentEvaluationController { // 학생 강의 평가 컨트롤러 -> 강의 평가 데이터를 읽거나 저장

    private final StudentEvaluationService service;

    // 질문 리스트 불러오기
    @GetMapping("/quelist")
    public ResponseEntity<List<EvaluationQuestionDTO>> readQuestionList() {
        List<EvaluationQuestionDTO> questionList = service.findAllList();
        return ResponseEntity.ok(questionList);
    }

    // 현재 학기의 강의 리스트 불러오기
    @GetMapping("/courselist")
    public ResponseEntity<List<EvaluationCourseDTO>> getCourseList(
            @RequestParam String studentId) {
        List<EvaluationCourseDTO> list = service.getCurrentSemesterCourses(studentId);
        return ResponseEntity.ok(list);
    }

    // 강의 평가 참여 여부 리스트 불러오기
    @GetMapping("/lecturelist")
    public ResponseEntity<List<EvaluationLectureDTO>> getLectureList(@RequestParam String studentId) {
        List<EvaluationLectureDTO> lectureList = service.findEvaluationStatus(studentId);
        return ResponseEntity.ok(lectureList);
    }

    // 데이터 저장
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/savedata")
    public ResponseEntity<Integer> saveEvaluationAnswer(
            @RequestParam String userId,
            @RequestParam Integer questionId,
            @RequestParam Integer classId,
            @RequestBody EvaluationWrapper wrapper
    ) {
        EvaluationLectureDTO lectureDto = wrapper.getLectDto();
        EvaluationAnswerDTO answerDto = wrapper.getDto();

        service.saveEvaluationLog(lectureDto, userId, classId);

        Integer savedId = service.saveEvaluationAnswer(userId, questionId, classId, lectureDto, answerDto);
        return ResponseEntity.ok(savedId);
    }

    // 내부 DTO 래퍼 클래스
    @Data
    public static class EvaluationWrapper {
        private EvaluationLectureDTO lectDto;
        private EvaluationAnswerDTO dto;
    }
}