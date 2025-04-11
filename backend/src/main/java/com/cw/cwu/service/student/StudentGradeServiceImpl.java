package com.cw.cwu.service.student;

import com.cw.cwu.domain.*;
import com.cw.cwu.dto.GradeDTO;
import com.cw.cwu.repository.*;
import com.cw.cwu.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentGradeServiceImpl implements StudentGradeService {

    private final StudentRecordRepository studentRecordRepository;
    private final GradeRepository gradeRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final SemesterRepository semesterRepository;

    // 학생 성적 조회
    // ConvertToDb로 ENUM 데이터 String으로 변환 (예시> A_PLUS -> A+)
    @Override
    public List<GradeDTO> getStudentGrade(String studentId) {
        return gradeRepository.findGrade(studentId)
                .stream()
                .map(grade -> {
                    GradeDTO dto = modelMapper.map(grade, GradeDTO.class);
                    String changeGrade = grade.ConvertToDb(StudentGrade.valueOf(grade.getGrade()));
                    dto.setGrade(changeGrade);
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
