package com.cw.cwu.service.professor;

import com.cw.cwu.domain.AnswerChoice;
import com.cw.cwu.domain.EvaluationAnswer;
import com.cw.cwu.domain.EvaluationLecture;
import com.cw.cwu.dto.EvaluationAnswerDTO;
import com.cw.cwu.dto.EvaluationLectureDTO;
import com.cw.cwu.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfessorEvaluationServiceImpl implements ProfessorEvaluationService {

    private final EvaluationQuestionRepository questionRepository;
    private final EvaluationAnswerRepository answerRepository;
    private final EvaluationLectureRepository lectureRepository;

    private final ClassEntityRepository classEntityRepository;
    private final UserRepository userRepository;
    private final SemesterRepository semesterRepository;
    private final EnrollmentRepository enrollmentRepository;

    // 강의 평가 리스트를 모두 가져오는 메서드
    // 필터링은 프론트에서 처리됨
    @Override
    public List<EvaluationLectureDTO> findAllLecture() {
        EvaluationLectureDTO dto = new EvaluationLectureDTO();
        List<EvaluationLecture> log = lectureRepository.findAll();
        List<EvaluationLectureDTO> lectureDtoList = new ArrayList<>();
        for (EvaluationLecture l : log) {
            lectureDtoList.add(domainToDTO(l));
        }
        return lectureDtoList;
    }

    // 강의 평가 내용을 아이디를 바탕으로 불러오는 메서드
    @Override
    public List<EvaluationAnswerDTO> findAnswersByEvaluationId(Integer evaluationId) {
        List<Object[]> rows = answerRepository.findAnswers(evaluationId);
        List<EvaluationAnswerDTO> dtos = new ArrayList<>();

        for (Object[] row : rows) {
            EvaluationAnswerDTO dto = new EvaluationAnswerDTO();

            dto.setAnswerId((Integer) row[0]);
            dto.setEvaluationId((Integer) row[1]);
            dto.setQuestionId((Integer) row[2]);

            Object choiceObj = row[3];
            dto.setAnswerChoice(choiceObj != null
                    ? dto.changeString(AnswerChoice.valueOf(choiceObj.toString()))
                    : "");

            dto.setSubjectiveText((String) row[4]);

            dtos.add(dto);
        }

        return dtos;
    }



    // dto 변환 메서드
    private EvaluationLectureDTO domainToDTO(EvaluationLecture l) {
        EvaluationLectureDTO dto = new EvaluationLectureDTO();
        dto.setEvaluationId(l.getEvaluationId());

        String stuId = (l.getStudentId() != null && l.getStudentId().getUserId() != null)
                ? l.getStudentId().getUserId()
                : "";
        dto.setStudentId(stuId);

        dto.setClassId(
                l.getClassId() != null ? l.getClassId().getId() : null
        );
        dto.setCreatedAt(l.getCreatedAt());

        return dto;
    }

    // DTO 변환 (EvaluationQuestion)
    private EvaluationAnswerDTO domainToDTO(EvaluationAnswer a) {
        EvaluationAnswerDTO dto = new EvaluationAnswerDTO();
        dto.setAnswerId(a.getAnswerId());
        dto.setEvaluationId(a.getEvaluationId().getEvaluationId());
        dto.setQuestionId(a.getQuestionId().getQuestionId());
        dto.setAnswerChoice(dto.changeString(a.getAnswerChoice()));
        dto.setSubjectiveText(dto.getSubjectiveText());
        return dto;
    }
}
