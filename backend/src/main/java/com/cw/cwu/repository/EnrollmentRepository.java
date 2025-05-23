package com.cw.cwu.repository;

import com.cw.cwu.domain.Enrollment;
import com.cw.cwu.domain.Semester;
import com.cw.cwu.domain.User;
import com.cw.cwu.domain.ClassEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    // 특정 학생이 특정 강의를 신청했는지 확인 (중복 방지)
    Optional<Enrollment> findByStudentAndEnrolledClassEntity(User student, ClassEntity enrolledClassEntity);

    // 특정 강의의 현재 신청된 인원 수 조회 (정원 초과 방지)
    int countByEnrolledClassEntity(ClassEntity enrolledClassEntity);

    // 학생이 신청한 모든 수강 내역 조회
    List<Enrollment> findByStudent(User student);

    boolean existsByStudentAndEnrolledClassEntity(User student, ClassEntity enrolledClassEntity);

    // 학생이 현재 신청한 총 학점 조회
    @Query("""
        SELECT COALESCE(SUM(e.enrolledClassEntity.course.credit), 0)
        FROM Enrollment e
        WHERE e.student.userId = :studentId
        AND e.enrolledClassEntity IS NOT NULL
        """)
    Integer getTotalCreditsByStudent(@Param("studentId") String studentId);

    // 시간표 중복 검사 (같은 요일, 시간이 겹치는 강의가 있는지)
    @Query("""
        SELECT COUNT(e) > 0 FROM Enrollment e
        WHERE e.student.userId = :studentId
        AND e.enrolledClassEntity IS NOT NULL
        AND e.enrolledClassEntity.day = :day
        AND (
         (:startTime BETWEEN e.enrolledClassEntity.startTime AND e.enrolledClassEntity.endTime) OR
         (:endTime BETWEEN e.enrolledClassEntity.startTime AND e.enrolledClassEntity.endTime) OR
         (e.enrolledClassEntity.startTime BETWEEN :startTime AND :endTime)
         )
         """)
    boolean existsByStudentAndTimeConflict(
            @Param("studentId") String studentId,
            @Param("day") String day,
            @Param("startTime") Integer startTime,
            @Param("endTime") Integer endTime
    );

    // 학생이 신청한 전공 과목 개수 조회
    @Query("""
        SELECT COUNT(e) FROM Enrollment e
        WHERE e.student.userId = :studentId
        AND e.enrolledClassEntity IS NOT NULL
        AND e.enrolledClassEntity.course.type = 'MAJOR'
        """)
    Integer countMajorCoursesByStudent(@Param("studentId") String studentId);


    // 특정 학생이 수강한 모든 과목 조회
    @Query("""
    SELECT e FROM Enrollment e
    WHERE e.student.userId = :studentId
    AND e.enrolledClassEntity IS NOT NULL
    AND e.enrolledClassEntity.semester.id = :semesterId
""")
    List<Enrollment> findEnrollmentsByStudentIdAndSemesterId(
            @Param("studentId") String studentId,
            @Param("semesterId") Integer semesterId
    );

    Page<Enrollment> findByEnrolledClassEntity_Id(Integer classId, Pageable pageable);

    // 학생 + 학기의 조건에 맞는 수강 정보 리스트를 조회 (2025.04.16 추가)
    List<Enrollment> findByStudentAndEnrolledClassEntity_Semester(User student, Semester current);

    // 해당 학번의 강의 지우기 (2025.04.21 추가)
    void deleteByStudent_UserId(String studentId);

    // 아이디 찾기(2025.04.25 추가)
    List<Enrollment> findByStudent_UserId(String studentId);



}
