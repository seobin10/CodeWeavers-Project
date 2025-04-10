package com.cw.cwu.service.admin;

import com.cw.cwu.domain.*;
import com.cw.cwu.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class AdminGradeServiceImpl implements AdminGradeService {

    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final SemesterRepository semesterRepository;
    private final StudentRecordRepository studentRecordRepository;
    private final DepartmentRepository departmentRepository;

    // 전체 학생에 대해 GPA 집계 실행
    @Transactional
    @Override
    public void finalizeStudentRecordsByDepartment(Integer semesterId, Integer departmentId) {
        // departmentId로 실제 Department 객체를 조회
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 학과를 찾을 수 없습니다."));

        // UserRole과 Department 객체로 조회
        List<User> students = userRepository.findByUserRoleAndDepartment(UserRole.STUDENT, department);

        for (User student : students) {
            try {
                updateStudentRecordAsAdmin(student.getUserId(), semesterId);
            } catch (IllegalStateException e) {
                // 성적 누락 등으로 인해 집계 실패한 학생은 skip
                System.out.println("성적 집계 실패: " + student.getUserId() + " - " + e.getMessage());
            }
        }
    }

    @Transactional
    @Override
    public void updateStudentRecordAsAdmin(String studentId, Integer semesterId) {
        // 해당 학생이 수강한 모든 강의 엔티티 조회
        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentsByStudentId(studentId);

        // 초기값 설정
        int totalEnrolled = 0;       // 총 수강 학점
        int totalEarned = 0;         // F 제외한 이수 학점
        double totalGradePoints = 0.0; // GPA 계산용 점수 누적
        int totalCredits = 0;        // GPA 분모가 될 학점 수

        for (Enrollment enrollment : enrollments) {
            ClassEntity classEntity = enrollment.getEnrolledClassEntity();

            // 강의가 없거나, 대상 학기가 아닌 경우 무시
            if (classEntity == null || !classEntity.getSemester().getId().equals(semesterId)) continue;

            // 성적 정보 미입력 시 예외 발생 → 전체 롤백
            Grade grade = enrollment.getGrade();
            if (grade == null || grade.getGrade() == null) {
                throw new IllegalStateException("성적이 입력되지 않은 과목이 존재합니다.");
            }

            // 과목 정보로부터 학점 획득
            Course course = classEntity.getCourse();
            int credit = course.getCredit();

            // 성적을 등급 점수로 변환
            double gradePoint = convertGradeToPoint(grade.getGrade());

            // 누적 집계
            totalEnrolled += credit;                  // 수강 학점
            totalCredits += credit;                   // GPA 계산용 총 학점
            totalGradePoints += gradePoint * credit;  // GPA 계산용 점수 누적
            if (gradePoint > 0.0) totalEarned += credit; // F 제외하고만 이수 처리
        }

        // GPA 계산 (학점이 0인 경우 0 처리)
        float gpa = totalCredits == 0 ? 0.0f : (float) (totalGradePoints / totalCredits);

        // 학생 및 학기 엔티티 조회
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 학생을 찾을 수 없습니다"));

        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new IllegalArgumentException("해당 학기를 찾을 수 없습니다"));

        // student_records 저장 객체 생성
        StudentRecord record = StudentRecord.builder()
                .student(student)
                .semester(semester)
                .enrolled(totalEnrolled)
                .earned(totalEarned)
                .gpa(gpa)
                .build();

        // 최종 저장
        studentRecordRepository.save(record);
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