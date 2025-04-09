package com.cw.cwu.repository;

import com.cw.cwu.domain.Grade;
import com.cw.cwu.dto.GradeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Integer> {

    // 학생 성적 조회 기능 추가
    @Query(value = """
        SELECT e.student_id, co.course_name, co.credit, g.grade_grade
        FROM enrollments e
        JOIN grades g ON e.enrollment_id = g.enrollment_id
        JOIN classes c ON e.class_id = c.class_id
        JOIN courses co ON c.course_id = co.course_id
        WHERE e.student_id = CAST(? AS CHAR)
    """, nativeQuery = true)
    List<GradeDTO> findGrade(String studentId);

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
