package com.cw.cwu.service.admin;

import com.cw.cwu.domain.Answer;
import com.cw.cwu.domain.Question;
import com.cw.cwu.domain.Status;
import com.cw.cwu.domain.User;
import com.cw.cwu.dto.AnswerDTO;
import com.cw.cwu.repository.AnswerRepository;
import com.cw.cwu.repository.QuestionRepository;
import com.cw.cwu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAnsServiceImpl implements AdminAnsService{
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    // 답변 작성
    @Override
    public Integer writeAns(AnswerDTO dto, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user id 데이터를 찾을 수 없습니다."));

        Question question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(()-> new RuntimeException("questionId 데이터를 찾을 수 없습니다."));

        // 중복 답변 방지
        if (question.getStatus() == Status.ANSWERED) {
            throw new RuntimeException("이미 답변이 작성된 질문입니다.");
        }

        // 답변 완료 상태로 변경
        question.setStatus(Status.ANSWERED);
        questionRepository.save(question);

        Answer answer = Answer.builder()
                .answerId(dto.getAnswerId())
                .questionId(
                        Question.builder()
                                .questionId(dto.getQuestionId())
                                .build()
                )
                .userId(user)
                .answer(dto.getAnswer())
                .answerDate(dto.getAnswerDate())
                .build();

        Answer result = answerRepository.save(answer);
        return result.getAnswerId();
    }

    // 답변 삭제
    @Override
    public void deleteAns(Integer questionId){
        Question question = questionRepository.findById(questionId)
                .orElseThrow(()-> new RuntimeException("해당하는 질문을 찾을 수 없습니다."));
        Answer answer = answerRepository.findByQuestionId(question)
                .orElseThrow(()->new RuntimeException("해당하는 답변을 찾을 수 없습니다."));

        // 미답변 상태로 변경
        question.setStatus(Status.OPEN);
        questionRepository.save(question);

        answerRepository.deleteById(answer.getAnswerId());
    }
}
