package com.cw.cwu.service.student;

import com.cw.cwu.domain.User;
import com.cw.cwu.domain.StudentRecord;
import com.cw.cwu.dto.StudentStatusDTO;
import com.cw.cwu.repository.StudentRecordRepository;
import com.cw.cwu.repository.UserRepository;
import com.cw.cwu.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentInfoServiceImpl implements StudentInfoService {

    private final StudentRecordRepository studentRecordRepository;
    private final UserRepository userRepository;

    // 학생 학년 계산 (StudentRecord 기반 누적 취득 학점)
    @Override
    public int calculateStudentYear(String studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 학생을 찾을 수 없습니다."));

        List<StudentRecord> records = studentRecordRepository.findByStudent(student);

        int totalEarned = records.stream()
                .mapToInt(StudentRecord::getEarned)
                .sum();

        if (totalEarned < 24) return 1;
        if (totalEarned < 48) return 2;
        if (totalEarned < 72) return 3;
        return 4;
    }

    // 학생 졸업 가능 여부 확인 (총 취득학점 >= 130)
    @Override
    public boolean checkGraduationEligibility(String studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 학생을 찾을 수 없습니다."));

        int totalEarned = studentRecordRepository.findByStudent(student)
                .stream()
                .mapToInt(StudentRecord::getEarned)
                .sum();

        return totalEarned >= 130;
    }

    // 학생 상태 정보 조회
    @Override
    public StudentStatusDTO getStudentStatusInfo(String studentId, String requesterId) {
        AuthUtil.checkOwnership(studentId, requesterId);

        int year = calculateStudentYear(studentId);
        boolean graduated = checkGraduationEligibility(studentId);

        StudentStatusDTO dto = new StudentStatusDTO();
        dto.setUserId(studentId);
        dto.setStudentYear(year);
        dto.setGraduationEligible(graduated);

        return dto;
    }
}