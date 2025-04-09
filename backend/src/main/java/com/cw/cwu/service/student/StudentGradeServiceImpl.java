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

    // 학생 성적 기록 업데이트 (학점, 취득 학점, GPA 계산 후 저장)
    @Override
    public void updateStudentRecords(String studentId, String requesterId) {
        AuthUtil.checkOwnership(studentId, requesterId);
        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentsByStudentId(studentId);

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

        // 동적으로 semester를 선택하도록 수정
        Semester semester = getCurrentSemester(); // 동적 방식으로 학기 선택
        studentRecord.setSemester(semester);

        studentRecord.setEnrolled(totalEnrolled);
        studentRecord.setEarned(totalEarned);
        studentRecord.setGpa(gpa);

        studentRecordRepository.save(studentRecord);
    }

    // 현재 학기를 계산하는 메서드
    private Semester getCurrentSemester() {
        LocalDate currentDate = LocalDate.now();

        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();

        // 1학기 (1월 ~ 6월)
        if (currentMonth >= 1 && currentMonth <= 6) {
            return semesterRepository.findByYearAndTerm(currentYear, SemesterTerm.FIRST)
                    .orElseThrow(() -> new IllegalArgumentException("현재 학기를 찾을 수 없습니다."));
        }
        // 2학기 (7월 ~ 12월)
        else {
            return semesterRepository.findByYearAndTerm(currentYear, SemesterTerm.SECOND)
                    .orElseThrow(() -> new IllegalArgumentException("현재 학기를 찾을 수 없습니다."));
        }
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
