package com.cw.cwu.service.student;

import com.cw.cwu.repository.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentInfoServiceImpl implements StudentInfoService {

    private final StudentRepository studentRepository;

    // 학생 학년 계산 (누적 학점 기반)
    public int calculateStudentYear(String studentId) {
        Integer totalCredits = studentRepository.findTotalEarnedCreditsByStudent(studentId);
        if (totalCredits == null || totalCredits < 24) return 1;
        if (totalCredits < 48) return 2;
        if (totalCredits < 72) return 3;
        return 4;
    }

    // 학생 졸업 가능 여부 확인 (130학점 이상 필요)
    public boolean checkGraduationEligibility(String studentId) {
        Integer totalCredits = studentRepository.findTotalEarnedCreditsByStudent(studentId);
        return totalCredits != null && totalCredits >= 130;
    }
}