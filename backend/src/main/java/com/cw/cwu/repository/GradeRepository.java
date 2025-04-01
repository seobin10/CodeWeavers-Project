package com.cw.cwu.repository;

import com.cw.cwu.domain.Grade;
import com.cw.cwu.dto.GradeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

}
