package com.cw.cwu.repository.student;

import com.cw.cwu.domain.Enrollment;
import com.cw.cwu.domain.StudentRecord;
import com.cw.cwu.dto.GradeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Map;

public interface StudentRepository extends JpaRepository<StudentRecord, Long> {

    // 학생의 총 취득 학점(SUM(earned)) 조회
    @Query("SELECT SUM(sr.earned) FROM StudentRecord sr WHERE sr.student.userId = :studentId")
    Integer findTotalEarnedCreditsByStudent(@Param("studentId") String studentId);

    // 특정 학생이 수강한 모든 과목 조회
    @Query("SELECT e FROM Enrollment e WHERE e.student.userId = :studentId AND e.enrolledClassEntity IS NOT NULL")
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

    // 학생이 수강 신청 가능한 강의 목록 조회
    @Query(value = """
SELECT 
    c.class_id AS 강의번호,
    CASE
        WHEN co.course_type = 'MAJOR' THEN '전공'
        WHEN co.course_type = 'LIBERAL' THEN '교양'
    END AS 구분,
    IFNULL(d.department_name, '공통') AS 개설학과,
    co.course_year AS 강의학년,
    co.course_name AS 강의명,
    c.class_day AS 강의요일, 
    CONCAT(b.building_name, ' ', lr.room_name) AS 강의실,
    GROUP_CONCAT(c.class_start + n.number ORDER BY n.number SEPARATOR ',') AS 강의시간,
    co.credit AS 강의학점,
    IFNULL(u.user_name, '미정') AS 담당교수,
    CONCAT(c.class_enrolled, '/', c.class_capacity) AS 수강인원
FROM classes c
JOIN courses co ON c.course_id = co.course_id
LEFT JOIN departments d ON co.department_id = d.department_id
LEFT JOIN lecture_rooms lr ON c.room_id = lr.room_id
LEFT JOIN buildings b ON lr.building_id = b.building_id
LEFT JOIN users u ON c.professor_id = u.user_id
JOIN (
    SELECT 0 AS number UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL 
    SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8
) n
ON n.number BETWEEN 0 AND (c.class_end - c.class_start)
WHERE 
    (:courseType IS NULL OR
        (co.course_type = 'MAJOR' AND :courseType = '전공') OR
        (co.course_type = 'LIBERAL' AND :courseType = '교양'))
    AND (:departmentName IS NULL OR IFNULL(d.department_name, '공통') = :departmentName)
    AND (:courseYear IS NULL OR co.course_year = :courseYear)
    AND (:classDay IS NULL OR c.class_day = :classDay) 
    AND (:classStart IS NULL OR c.class_start = :classStart)
    AND (:credit IS NULL OR co.credit = :credit)
    AND (:courseName IS NULL OR co.course_name LIKE %:courseName%)
GROUP BY c.class_id
ORDER BY c.class_id
""", countQuery = """
SELECT COUNT(DISTINCT c.class_id)
FROM classes c
JOIN courses co ON c.course_id = co.course_id
LEFT JOIN departments d ON co.department_id = d.department_id
WHERE 
    (:courseType IS NULL OR
        (co.course_type = 'MAJOR' AND :courseType = '전공') OR
        (co.course_type = 'LIBERAL' AND :courseType = '교양'))
    AND (:departmentName IS NULL OR IFNULL(d.department_name, '공통') = :departmentName)
    AND (:courseYear IS NULL OR co.course_year = :courseYear)
    AND (:classDay IS NULL OR c.class_day = :classDay) 
    AND (:classStart IS NULL OR c.class_start = :classStart)
    AND (:credit IS NULL OR co.credit = :credit)
    AND (:courseName IS NULL OR co.course_name LIKE %:courseName%)
""",
            nativeQuery = true)
    Page<Map<String, Object>> findAvailableCoursesPaged(
            @Param("studentId") String studentId,
            @Param("courseType") String courseType,
            @Param("departmentName") String departmentName,
            @Param("courseYear") Integer courseYear,
            @Param("classDay") String classDay,
            @Param("classStart") Integer classStart,
            @Param("credit") Integer credit,
            @Param("courseName") String courseName,
            org.springframework.data.domain.Pageable pageable
    );
}

