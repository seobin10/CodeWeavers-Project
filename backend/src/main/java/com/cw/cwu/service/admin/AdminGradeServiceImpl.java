package com.cw.cwu.service.admin;

import com.cw.cwu.domain.*;
import com.cw.cwu.dto.GradeStatusDTO;
import com.cw.cwu.dto.SemesterDTO;
import com.cw.cwu.repository.*;
import com.cw.cwu.service.user.UserSemesterService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminGradeServiceImpl implements AdminGradeService {

    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final SemesterRepository semesterRepository;
    private final StudentRecordRepository studentRecordRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;
    private final UserSemesterService userSemesterService;
    private final ScheduleSettingRepository scheduleSettingRepository;


    /**
     * 전체 학생에 대해 GPA 집계 실행 (학과 기준)
     * - 성적이 하나라도 누락된 학생이 있다면 전체 집계를 중단하고 예외 발생
     * - 이미 집계된 학생이 존재하면 값 비교 후 동일하면 생략, 다르면 기존 record를 삭제 후 재저장
     */
    @Transactional
    @Override
    public void finalizeStudentRecordsByDepartment(Integer semesterId, Integer departmentId) {
        // 현재 집계 가능한 시점인지 확인
        Semester current = userSemesterService.getCurrentSemester();
        if (!semesterId.equals(current.getId())) {
            throw new IllegalStateException("현재 학기에만 성적을 집계할 수 있습니다!");
        }

        LocalDateTime now = LocalDateTime.now();

        ScheduleSetting gradeSchedule = scheduleSettingRepository
                .findBySemesterIdAndScheduleType(current.getId(), ScheduleType.GRADE)
                .orElseThrow(() -> new IllegalStateException("성적 입력 일정이 존재하지 않습니다."));

        if (now.isBefore(gradeSchedule.getStartDate()) ||
                now.isAfter(current.getEndDate().atTime(23, 59, 59))) {
            throw new IllegalStateException("성적 집계는 성적 입력 기간이 시작된 이후부터 학기 종료 전까지만 가능합니다.");
        }

        // 1. 학과 조회
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 학과를 찾을 수 없습니다."));

        // 2. 학기 조회
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new IllegalArgumentException("해당 학기를 찾을 수 없습니다."));


        // 3. 해당 학과의 전체 학생 조회
        List<User> students = userRepository.findByUserRoleAndDepartment(UserRole.STUDENT, department);

        // 4. 성적 누락 여부 검사
        List<String> invalidStudents = new ArrayList<>();
        for (User student : students) {
            List<Enrollment> enrollments = enrollmentRepository.findEnrollmentsByStudentId(student.getUserId());
            for (Enrollment enrollment : enrollments) {
                ClassEntity classEntity = enrollment.getEnrolledClassEntity();
                if (classEntity == null || !classEntity.getSemester().getId().equals(semesterId)) continue;

                Grade grade = enrollment.getGrade();
                if (grade == null || grade.getGrade() == null) {
                    invalidStudents.add(student.getUserId());
                    break;
                }
            }
        }

        // 5. 누락된 학생이 있다면 전체 집계 중단
        if (!invalidStudents.isEmpty()) {
            throw new IllegalStateException("성적이 미입력된 학생이 있어 집계를 진행할 수 없습니다.");
        }

        // 6. 검증 통과한 학생들 성적 집계 실행
        for (User student : students) {
            updateStudentRecordAsAdmin(student.getUserId(), semesterId);
        }
    }

    /**
     * 단일 학생의 성적 집계 후 저장
     * - 기존 기록이 있다면 GPA, 학점 비교 후 동일하면 생략
     * - 값이 다르면 기존 record 삭제 후 재저장
     */
    @Transactional
    @Override
    public void updateStudentRecordAsAdmin(String studentId, Integer semesterId) {
        // 1. 수강 정보 조회
        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentsByStudentId(studentId);

        // 2. 초기값 설정
        int totalEnrolled = 0;
        int totalEarned = 0;
        double totalGradePoints = 0.0;
        int totalCredits = 0;
        boolean hasValidEnrollment = false;

        for (Enrollment enrollment : enrollments) {
            ClassEntity classEntity = enrollment.getEnrolledClassEntity();
            if (classEntity == null || !classEntity.getSemester().getId().equals(semesterId)) continue;

            Grade grade = enrollment.getGrade();
            if (grade == null || grade.getGrade() == null) {
                throw new IllegalStateException("성적이 입력되지 않은 과목이 존재합니다.");
            }

            hasValidEnrollment = true;
            Course course = classEntity.getCourse();
            int credit = course.getCredit();
            double gradePoint = convertGradeToPoint(grade.getGrade());

            totalEnrolled += credit;
            totalCredits += credit;
            totalGradePoints += gradePoint * credit;
            if (gradePoint > 0.0) totalEarned += credit;
        }

        // 3. 수강 내역이 없다면 저장하지 않음
        if (!hasValidEnrollment) {
            System.out.println("수강 내역 없음. 학생 ID: " + studentId);
            return;
        }

        // 4. GPA 계산
        float gpa = totalCredits == 0 ? 0.0f : (float) (totalGradePoints / totalCredits);

        // 5. 엔티티 조회
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 학생을 찾을 수 없습니다"));
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new IllegalArgumentException("해당 학기를 찾을 수 없습니다"));

        final int finalEnrolled = totalEnrolled;
        final int finalEarned = totalEarned;
        final float finalGpa = gpa;


        // 6. 기존 성적 기록이 있다면 값 비교 후 동일하면 생략, 다르면 삭제
        studentRecordRepository.findByStudentAndSemester(student, semester)
                .ifPresent(existing -> {
                    if (existing.getEnrolled() == finalEnrolled &&
                            existing.getEarned() == finalEarned &&
                            existing.getGpa() == finalGpa) {
                        throw new IllegalStateException("이미 동일한 성적이 집계되어 있습니다.");
                    }
                    studentRecordRepository.delete(existing);
                    studentRecordRepository.flush();
                    System.out.println("기존 성적 기록 덮어씀. 학생: " + studentId);
                });

        // 7. 새 성적 기록 저장
        StudentRecord record = StudentRecord.builder()
                .student(student)
                .semester(semester)
                .enrolled(totalEnrolled)
                .earned(totalEarned)
                .gpa(gpa)
                .build();

        studentRecordRepository.save(record);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GradeStatusDTO> getGradeStatusSummary(Integer semesterId, Integer departmentId) {
        // 학기 & 학과 조회
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new IllegalArgumentException("해당 학기를 찾을 수 없습니다."));
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 학과를 찾을 수 없습니다."));

        // 학과 소속 전체 학생 조회
        List<User> students = userRepository.findByUserRoleAndDepartment(UserRole.STUDENT, department);
        List<GradeStatusDTO> result = new ArrayList<>();

        for (User student : students) {
            List<Enrollment> enrollments = enrollmentRepository.findEnrollmentsByStudentId(student.getUserId());

            int missingCount = 0;
            int totalCredits = 0;
            int totalEarned = 0;
            double totalGradePoints = 0.0;

            for (Enrollment enrollment : enrollments) {
                ClassEntity classEntity = enrollment.getEnrolledClassEntity();
                if (classEntity == null || !classEntity.getSemester().getId().equals(semesterId)) continue;

                Grade grade = enrollment.getGrade();
                Course course = classEntity.getCourse();
                int credit = course.getCredit();

                if (grade == null || grade.getGrade() == null) {
                    missingCount++;
                    continue;
                }

                double point = convertGradeToPoint(grade.getGrade());
                totalCredits += credit;
                totalGradePoints += point * credit;
                if (point > 0.0) totalEarned += credit;
            }

            Float calculatedGpa = (totalCredits == 0) ? null : (float)(totalGradePoints / totalCredits);

            // 기존 기록 조회
            Optional<StudentRecord> recordOpt = studentRecordRepository.findByStudentAndSemester(student, semester);
            Float recordedGpa = recordOpt.map(StudentRecord::getGpa).orElse(null);

            // 상태 판별
            String status;
            if (missingCount > 0) {
                status = "미입력";
            } else if (recordedGpa != null && !recordedGpa.equals(calculatedGpa)) {
                status = "수정";
            } else {
                status = "이상없음";
            }

            if (!status.equals("이상없음")) {
                result.add(GradeStatusDTO.builder()
                        .studentId(student.getUserId())
                        .studentName(student.getUserName())
                        .departmentName(department.getDepartmentName())
                        .status(status)
                        .recordedGpa(recordedGpa)
                        .calculatedGpa(calculatedGpa)
                        .missingGradesCount(missingCount)
                        .build());
            }
        }

        return result;
    }

    @Override
    public SemesterDTO getCurrentSemesterDTO() {
        Semester semester = userSemesterService.getCurrentSemester();
        return modelMapper.map(semester, SemesterDTO.class);
    }

    /**
     * 성적 등급을 GPA 점수로 변환
     */
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