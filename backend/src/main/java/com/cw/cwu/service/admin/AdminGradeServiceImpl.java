package com.cw.cwu.service.admin;

import com.cw.cwu.domain.*;
import com.cw.cwu.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class AdminGradeServiceImpl implements AdminGradeService {

    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final SemesterRepository semesterRepository;
    private final StudentRecordRepository studentRecordRepository;
    private final DepartmentRepository departmentRepository;

    // 전체 학생에 대해 GPA 집계 실행 (학과 기준)
    // 성적이 하나라도 누락된 학생이 있다면 전체 집계를 중단하고 예외 발생
    @Transactional
    @Override
    public void finalizeStudentRecordsByDepartment(Integer semesterId, Integer departmentId) {
        // departmentId로 실제 Department 객체를 조회
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 학과를 찾을 수 없습니다."));

        // UserRole과 Department 객체로 학생 목록 조회
        List<User> students = userRepository.findByUserRoleAndDepartment(UserRole.STUDENT, department);


        //학기 조회
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new IllegalArgumentException("해당 학기를 찾을 수 없습니다."));

        // 이미 해당 학기 성적이 집계된 학생이 존재하면 중단
        boolean alreadyFinalized = students.stream()
                .anyMatch(student -> studentRecordRepository.existsByStudentAndSemester(student, semester));

        if (alreadyFinalized) {
            throw new IllegalStateException("해당 학과는 이미 성적 집계가 완료되었습니다.");
        }

        // 성적 미입력된 학생 ID 목록
        List<String> invalidStudents = new ArrayList<>();

        // 먼저 전체 학생에 대해 성적 누락 여부 사전 검사
        for (User student : students) {
            List<Enrollment> enrollments = enrollmentRepository.findEnrollmentsByStudentId(student.getUserId());

            for (Enrollment enrollment : enrollments) {
                ClassEntity classEntity = enrollment.getEnrolledClassEntity();

                // 강의가 없거나, 대상 학기가 아닌 경우 무시
                if (classEntity == null || !classEntity.getSemester().getId().equals(semesterId)) continue;

                // 성적 정보가 없으면 유효하지 않은 학생으로 간주 > 목록 추가 후 break
                Grade grade = enrollment.getGrade();
                if (grade == null || grade.getGrade() == null) {
                    invalidStudents.add(student.getUserId());
                    break; // 이 학생은 이미 집계 불가 > 추가 검사 생략
                }
            }
        }

        // 성적 미입력 학생이 존재할 경우 전체 집계를 중단하고 예외 발생
        if (!invalidStudents.isEmpty()) {
            throw new IllegalStateException("성적이 미입력된 학생이 있습니다. \n\n학생 ID: " + String.join(", ", invalidStudents));
        }

        // 모든 검증을 통과한 학생에 대해 GPA 집계 실행
        for (User student : students) {
            updateStudentRecordAsAdmin(student.getUserId(), semesterId);
        }
    }

    @Transactional
    @Override
    public void updateStudentRecordAsAdmin(String studentId, Integer semesterId) {
        // 1. 해당 학생이 수강한 모든 강의 조회
        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentsByStudentId(studentId);

        // 2. 초기값 설정
        int totalEnrolled = 0;         // 총 수강 학점
        int totalEarned = 0;           // F 제외한 이수 학점
        double totalGradePoints = 0.0; // GPA 계산용 점수 누적
        int totalCredits = 0;          // GPA 분모가 될 학점 수

        boolean hasValidEnrollment = false; // 유효한 수강 내역 존재 여부 체크

        for (Enrollment enrollment : enrollments) {
            ClassEntity classEntity = enrollment.getEnrolledClassEntity();

            // 3. 대상 학기가 아닌 경우 무시
            if (classEntity == null || !classEntity.getSemester().getId().equals(semesterId)) continue;

            // 4. 과목 성적 확인 (null 이면 예외 던지기)
            Grade grade = enrollment.getGrade();
            if (grade == null || grade.getGrade() == null) {
                throw new IllegalStateException("성적이 입력되지 않은 과목이 존재합니다.");
            }

            hasValidEnrollment = true; // 유효한 수강 내역 존재

            // 5. 학점 및 GPA 계산
            Course course = classEntity.getCourse();
            int credit = course.getCredit();
            double gradePoint = convertGradeToPoint(grade.getGrade());

            totalEnrolled += credit;
            totalCredits += credit;
            totalGradePoints += gradePoint * credit;
            if (gradePoint > 0.0) totalEarned += credit;
        }

        // 6. 수강 내역이 전혀 없다면 저장하지 않음
        if (!hasValidEnrollment) {
            System.out.println(" 수강 내역 없음 → 학생 ID: " + studentId + " → 성적 집계 생략");
            return;
        }

        // 7. GPA 계산 (총 학점이 0인 경우 0 처리)
        float gpa = totalCredits == 0 ? 0.0f : (float) (totalGradePoints / totalCredits);

        // 8. 엔티티 조회
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 학생을 찾을 수 없습니다"));
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new IllegalArgumentException("해당 학기를 찾을 수 없습니다"));


        // 9. 성적 기록 생성 및 저장
        StudentRecord record = StudentRecord.builder()
                .student(student)
                .semester(semester)
                .enrolled(totalEnrolled)
                .earned(totalEarned)
                .gpa(gpa)
                .build();

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