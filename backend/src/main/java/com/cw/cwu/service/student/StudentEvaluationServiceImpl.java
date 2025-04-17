package com.cw.cwu.service.student;

import com.cw.cwu.domain.*;
import com.cw.cwu.dto.EvaluationAnswerDTO;
import com.cw.cwu.dto.EvaluationCourseDTO;
import com.cw.cwu.dto.EvaluationLectureDTO;
import com.cw.cwu.dto.EvaluationQuestionDTO;
import com.cw.cwu.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentEvaluationServiceImpl implements StudentEvaluationService {

    private final EvaluationQuestionRepository questionRepository;
    private final EvaluationAnswerRepository answerRepository;
    private final EvaluationLectureRepository lectureRepository;

    private final ClassEntityRepository classEntityRepository;
    private final UserRepository userRepository;
    private final SemesterRepository semesterRepository;
    private final EnrollmentRepository enrollmentRepository;

    // 강의 평가 질문 리스트 가져오기
    @Override
    public List<EvaluationQuestionDTO> findAllList() {
        EvaluationQuestionDTO dto = new EvaluationQuestionDTO();
        List<EvaluationQuestion> question = questionRepository.findAll();
        List<EvaluationQuestionDTO> questionList = new ArrayList<>();
        for (EvaluationQuestion q : question) {
            questionList.add(domainToDTO(q));
        }
        return questionList;
    }

    // 학기에 해당하는 과목 가져오기
    @Override
    public List<EvaluationCourseDTO> getCurrentSemesterCourses(String studentId) {
        User student = userRepository.findByUserId(studentId).orElseThrow(() -> new IllegalArgumentException("학생 정보를 찾을 수 없습니다."));

        Semester current = semesterRepository.findCurrentSemester(LocalDate.now()).orElseThrow(() -> new IllegalStateException("현재 학기를 찾을 수 없습니다."));

        List<Enrollment> enrollments = enrollmentRepository.findByStudentAndEnrolledClassEntity_Semester(student, current);

        return enrollments.stream().map(e -> {
            ClassEntity c = e.getEnrolledClassEntity();
            return new EvaluationCourseDTO(c.getId(), c.getCourse().getName(), c.getProfessor().getUserName(), c.getCourse().getCredit());
        }).collect(Collectors.toList());
    }

    // 강의 평가 내용 저장하기
    @Override
    public Integer saveEvaluationAnswer(String userId, Integer questionId, Integer classId,
                                        EvaluationLectureDTO lectDto, EvaluationAnswerDTO dto) {
        User student = userRepository.findById(userId).orElseThrow();
        ClassEntity classEntity = classEntityRepository.findById(classId).orElseThrow();

        EvaluationLecture evaluation = lectureRepository.findByStudentIdAndClassId(student, classEntity)
                .orElseThrow(() -> new RuntimeException("저장된 로그 값이 없습니다."));

        EvaluationQuestion question = questionRepository.findById(questionId).orElseThrow();

        EvaluationAnswer answer = EvaluationAnswer.builder()
                .answerId(dto.getAnswerId())
                .evaluationId(evaluation)
                .questionId(question)
                .answerChoice(
                        dto.getAnswerChoice() == null
                                ? null
                                : dto.changeEnum(dto.getAnswerChoice())
                )
                .subjectiveText(dto.getSubjectiveText())
                .build();

        return answerRepository.save(answer).getAnswerId();
    }


    // 강의 평가 내역 저장하기 (EvaluationLecture)
    @Override
    @Transactional
    public EvaluationLecture saveEvaluationLog(EvaluationLectureDTO dto, String userId, Integer classId) {
        User student = userRepository.findById(userId).orElseThrow();
        ClassEntity classEntity = classEntityRepository.findById(classId).orElseThrow();

        return lectureRepository.findByStudentIdAndClassId(student, classEntity).orElseGet(() -> {
            EvaluationLecture newLog = EvaluationLecture.builder().studentId(student).classId(classEntity).createdAt(dto.getCreatedAt()).build();
            return lectureRepository.save(newLog);
        });
    }

    // 강의 평가 완료 여부 체크를 위해 관련 데이터 불러오기
    @Override
    public List<EvaluationLectureDTO> findEvaluationStatus(String studentId) {
        EvaluationLectureDTO dto = new EvaluationLectureDTO();
        List<EvaluationLecture> filteredList = lectureRepository.findAll().stream()
                .filter(e -> e.getStudentId().getUserId().equals(studentId))
                .toList();
        List<EvaluationLectureDTO> lectList = new ArrayList<>();
        for (EvaluationLecture l : filteredList) {
            lectList.add(domainToDTO(l));
        }
        return lectList;
    }

    // DTO 변환 (EvaluationQuestion)
    private EvaluationQuestionDTO domainToDTO(EvaluationQuestion q) {
        EvaluationQuestionDTO dto = new EvaluationQuestionDTO();
        dto.setQuestion_id(q.getQuestionId());
        dto.setQuestionText(q.getQuestionText());
        dto.setIsSubjective(q.getIsSubjective());
        return dto;
    }

    // DTO 변환 (EvaluationLecture)
    private EvaluationLectureDTO domainToDTO(EvaluationLecture l) {
        EvaluationLectureDTO dto = new EvaluationLectureDTO();
        dto.setEvaluationId(l.getEvaluationId());
        dto.setStudentId(l.getStudentId().getUserId());
        dto.setClassId(l.getClassId().getId());
        dto.setCreatedAt(l.getCreatedAt());
        return dto;
    }
}
