package com.cw.cwu.service.student;

import com.cw.cwu.dto.StudentStatusDTO;
import com.cw.cwu.repository.GradeRepository;
import com.cw.cwu.repository.StudentRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentInfoServiceImpl implements StudentInfoService {

    private final GradeRepository gradeRepository;

    // 학생 학년 계산 (누적 학점 기반)
    @Override
    public int calculateStudentYear(String studentId) {
        Integer totalCredits = gradeRepository.findTotalEarnedCreditsByStudent(studentId);
        if (totalCredits == null || totalCredits < 24) return 1;
        if (totalCredits < 48) return 2;
        if (totalCredits < 72) return 3;
        return 4;
    }

    // 학생 졸업 가능 여부 확인 (130학점 이상 필요)
    @Override
    public boolean checkGraduationEligibility(String studentId) {
        Integer totalCredits = gradeRepository.findTotalEarnedCreditsByStudent(studentId);
        return totalCredits != null && totalCredits >= 130;
    }

    @Override
    public StudentStatusDTO getStudentStatusInfo(String studentId) {
        int year = calculateStudentYear(studentId);
        boolean graduated = checkGraduationEligibility(studentId);

        StudentStatusDTO dto = new StudentStatusDTO();
        dto.setUserId(studentId);
        dto.setStudentYear(year);
        dto.setGraduationEligible(graduated);

        return dto;
    }


}