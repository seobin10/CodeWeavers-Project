package com.cw.cwu.service.student;

import com.cw.cwu.repository.StudentRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentInfoServiceImpl implements StudentInfoService {

    private final StudentRecordRepository studentRecordRepository;

    // 학생 학년 계산 (누적 학점 기반)
    @Override
    public int calculateStudentYear(String studentId) {
        Integer totalCredits = studentRecordRepository.findTotalEarnedCreditsByStudent(studentId);
        if (totalCredits == null || totalCredits < 24) return 1;
        if (totalCredits < 48) return 2;
        if (totalCredits < 72) return 3;
        return 4;
    }

    // 학생 졸업 가능 여부 확인 (130학점 이상 필요)
    @Override
    public boolean checkGraduationEligibility(String studentId) {
        Integer totalCredits = studentRecordRepository.findTotalEarnedCreditsByStudent(studentId);
        return totalCredits != null && totalCredits >= 130;
    }
}