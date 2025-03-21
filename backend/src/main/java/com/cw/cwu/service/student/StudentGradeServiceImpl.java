package com.cw.cwu.service.student;

import com.cw.cwu.domain.*;
import com.cw.cwu.dto.GradeDTO;
import com.cw.cwu.repository.student.StudentRepository;
import com.cw.cwu.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentGradeServiceImpl implements StudentGradeService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // 학생 성적 조회
    // ConvertToDb로 ENUM 데이터 String으로 변환 (예시> A_PLUS -> A+)
    public List<GradeDTO> getStudentGrade(String studentId) {
        return studentRepository.findGrade(studentId)
                .stream()
                .map(grade -> {
                    GradeDTO dto = modelMapper.map(grade, GradeDTO.class);
                    String changeGrade = grade.ConvertToDb(StudentGrade.valueOf(grade.getGrade()));
                    dto.setGrade(changeGrade);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 학생 성적 기록 업데이트 (학점, 취득 학점, GPA 계산 후 저장)
    public void updateStudentRecords(String studentId) {
        List<Enrollment> enrollments = studentRepository.findEnrollmentsByStudentId(studentId);

        int totalEnrolled = 0;
        int totalEarned = 0;
        double totalGradePoints = 0.0;
        int totalCredits = 0;

        for (Enrollment enrollment : enrollments) {
            Course course = enrollment.getEnrolledClassEntity().getCourse();
            int credit = course.getCredit();
            totalEnrolled += credit;

            Grade grade = Optional.ofNullable(enrollment.getGrade()).orElse(new Grade());
            double gradePoint = convertGradeToPoint(grade.getGrade());

            if (gradePoint > 0.0) {
                totalEarned += credit;
            }
            totalGradePoints += (gradePoint * credit);
            totalCredits += credit;
        }

        float gpa = totalCredits == 0 ? 0.0f : (float) (totalGradePoints / totalCredits);

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 학생을 찾을 수 없습니다: " + studentId));

        StudentRecord studentRecord = modelMapper.map(student, StudentRecord.class);
        studentRecord.setSemester("2024-1");
        studentRecord.setEnrolled(totalEnrolled);
        studentRecord.setEarned(totalEarned);
        studentRecord.setGpa(gpa);

        studentRepository.save(studentRecord);
    }

    private double convertGradeToPoint(StudentGrade grade) {
        if (grade == null) return 0.0;
        return switch (grade) {
            case A_PLUS -> 4.5;
            case A0 -> 4.0;
            case B_PLUS -> 3.5;
            case B0 -> 3.0;
            case C_PLUS -> 2.5;
            case C0 -> 2.0;
            case D_PLUS -> 1.5;
            case D0 -> 1.0;
            case F -> 0.0;
        };
    }
}
