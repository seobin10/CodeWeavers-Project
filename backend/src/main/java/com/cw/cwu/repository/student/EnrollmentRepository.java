package com.cw.cwu.repository.student;

import com.cw.cwu.domain.Enrollment;
import com.cw.cwu.domain.User;
import com.cw.cwu.domain.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    boolean existsByStudentAndEnrolledClassEntity(User student, ClassEntity enrolledClassEntity);

    Integer getTotalCreditsByStudent(User student);

    // 학생이 현재 신청한 총 학점 조회
    @Query("SELECT COALESCE(SUM(e.enrolledClassEntity.course.credit), 0) FROM Enrollment e WHERE e.student.userId = :studentId")
    Integer getTotalCreditsByStudent(@Param("studentId") String studentId);

    // 시간표 중복 검사 (같은 요일, 시간이 겹치는 강의가 있는지)
    @Query("""
        SELECT COUNT(e) > 0 FROM Enrollment e
        WHERE e.student.userId = :studentId
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
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.student.userId = :studentId AND e.enrolledClassEntity.course.type = 'MAJOR'")
    Integer countMajorCoursesByStudent(@Param("studentId") String studentId);

}
