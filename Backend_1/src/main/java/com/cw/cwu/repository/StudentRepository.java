package com.cw.cwu.repository;

import com.cw.cwu.domain.Enrollment;
import com.cw.cwu.domain.Grade;
import com.cw.cwu.domain.StudentRecord;
import com.cw.cwu.dto.GradeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface StudentRepository extends JpaRepository<StudentRecord, Long> {

    // 학생의 총 취득 학점(SUM(earned)) 조회
    @Query("SELECT SUM(sr.earned) FROM StudentRecord sr WHERE sr.student.userId = :studentId")
    Integer findTotalEarnedCreditsByStudent(@Param("studentId") String studentId);


    //특정 학생이 수강한 모든 과목 조회
    @Query("SELECT e FROM Enrollment e WHERE e.student.userId = :studentId")
    List<Enrollment> findEnrollmentsByStudentId(@Param("studentId") String studentId);

    // 학생 성적 조회 기능 추가 (기존 GradeRepository에서 가져옴)
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
