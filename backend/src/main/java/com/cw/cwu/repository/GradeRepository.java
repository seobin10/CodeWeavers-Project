package com.cw.cwu.repository;

import com.cw.cwu.domain.Grade;
import com.cw.cwu.dto.GradeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Integer> {


    // 현재 학기 성적 조회 (studentId + semesterId)
    @Query("""
    SELECT g
    FROM Grade g
    JOIN FETCH g.enrollment e
    JOIN FETCH e.enrolledClassEntity c
    JOIN FETCH c.course co
    WHERE e.student.userId = :studentId
      AND c.semester.id = :semesterId
""")
    List<Grade> findGrade(@Param("studentId") String studentId, @Param("semesterId") Integer semesterId);

    // 특정 학기 성적 조회 (semesterId 추가)
    @Query("""
    SELECT g
    FROM Grade g
    JOIN FETCH g.enrollment e
    JOIN FETCH e.enrolledClassEntity c
    JOIN FETCH c.course co
    WHERE e.student.userId = :studentId
      AND c.semester.id = :semesterId
""")
    List<Grade> findGradesByStudentAndSemester(@Param("studentId") String studentId, @Param("semesterId") Integer semesterId);

}
