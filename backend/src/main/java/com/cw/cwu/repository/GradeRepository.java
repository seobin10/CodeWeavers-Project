package com.cw.cwu.repository;

import com.cw.cwu.domain.Grade;
import com.cw.cwu.dto.GradeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Integer> {


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

//    // 학생 성적 조회 기능 추가
//    @Query("""
//    SELECT g
//    FROM Grade g
//    JOIN FETCH g.enrollment e
//    JOIN FETCH e.enrolledClassEntity c
//    JOIN FETCH c.course co
//    WHERE e.student.userId = :studentId
//""")
//    List<Grade> findGrade(@Param("studentId") String studentId);

    // 학생의 총 취득 학점(SUM(earned)) 조회 (F학점 제외)
    @Query("""
    SELECT SUM(c.credit)
    FROM Grade g
    JOIN g.enrollment e
    JOIN e.enrolledClassEntity ce
    JOIN ce.course c
    WHERE e.student.userId = :studentId
      AND g.grade <> 'F'
""")
    Integer findTotalEarnedCreditsByStudent(@Param("studentId") String studentId);
}
