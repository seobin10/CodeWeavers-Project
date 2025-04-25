package com.cw.cwu.repository;

import com.cw.cwu.domain.ClassEntity;
import com.cw.cwu.domain.Semester;
import com.cw.cwu.domain.SemesterTerm;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ClassEntityRepository extends JpaRepository<ClassEntity, Integer> {

    Page<ClassEntity> findByProfessor_UserId(String professorId, Pageable pageable);

    @Query("SELECT DISTINCT c.type FROM Course c")
    List<String> findDistinctCourseTypes();

    @Query("SELECT DISTINCT cl.day FROM ClassEntity cl")
    List<String> findDistinctClassDays();

    @Query("SELECT DISTINCT cl.startTime FROM ClassEntity cl ORDER BY cl.startTime")
    List<Integer> findDistinctClassTimes();

    @Query("SELECT DISTINCT c.credit FROM Course c ORDER BY c.credit")
    List<Integer> findDistinctCredits();

    // 학생이 수강 신청 가능한 강의 목록 조회
    @Query(value = """
SELECT\s
    c.class_id AS 강의번호,
    CASE
        WHEN co.course_type = 'MAJOR' THEN '전공'
        WHEN co.course_type = 'LIBERAL' THEN '교양'
    END AS 구분,
    IFNULL(d.department_name, '공통') AS 개설학과,
    co.course_year AS 강의학년,
    co.course_name AS 강의명,
    c.class_day AS 강의요일,\s
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
    SELECT 0 AS number UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL\s
    SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL\s
    SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8
) n
ON n.number BETWEEN 0 AND (c.class_end - c.class_start)
WHERE\s
    c.semester_id = :semesterId
    AND (:courseType IS NULL OR
        (co.course_type = 'MAJOR' AND :courseType = '전공') OR
        (co.course_type = 'LIBERAL' AND :courseType = '교양'))
   \s
    -- 전공이면 학과 매칭, 교양이면 무시
    AND (
        co.course_type = 'LIBERAL'
        OR (co.course_type = 'MAJOR' AND d.department_name = :departmentName)
    )

    -- 본인 학년 이하만
    AND co.course_year <= :studentYear
    AND (:classDay IS NULL OR c.class_day = :classDay)\s
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
WHERE\s
    c.semester_id = :semesterId
    AND (:courseType IS NULL OR
        (co.course_type = 'MAJOR' AND :courseType = '전공') OR
        (co.course_type = 'LIBERAL' AND :courseType = '교양'))
    AND (
        co.course_type = 'LIBERAL'
        OR (co.course_type = 'MAJOR' AND d.department_name = :departmentName)
    )
    AND co.course_year <= :studentYear
    AND (:classDay IS NULL OR c.class_day = :classDay)\s
    AND (:classStart IS NULL OR c.class_start = :classStart)
    AND (:credit IS NULL OR co.credit = :credit)
    AND (:courseName IS NULL OR co.course_name LIKE %:courseName%)
""",
            nativeQuery = true)
    Page<Map<String, Object>> findAvailableCoursesPaged(
            @Param("courseType") String courseType,
            @Param("departmentName") String departmentName,
            @Param("studentYear") Integer studentYear,
            @Param("classDay") String classDay,
            @Param("classStart") Integer classStart,
            @Param("credit") Integer credit,
            @Param("courseName") String courseName,
            @Param("semesterId") Integer semesterId,
            Pageable pageable
    );


    // 강의실 + 요일
    List<ClassEntity> findByLectureRoom_IdAndDayAndSemester(Integer roomId, String day, Semester semester);

    // 교수 + 요일
    List<ClassEntity> findByProfessor_UserIdAndDayAndSemester(String userId, String day, Semester semester);

    // 학기 ID를 기준으로 필터링
    Page<ClassEntity> findByProfessor_UserIdAndSemester_Id(String professorId, Integer semesterId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from ClassEntity c where c.id = :classId")
    Optional<ClassEntity> findByClassIdWithLock(@Param("classId") Integer classId);

    List<ClassEntity> findByLectureRoom_IdAndSemester_Id(Integer roomId, Integer semesterId);

    boolean existsByLectureRoom_Id(Integer lectureRoomId);

    boolean existsByLectureRoom_IdAndSemester_Id(Integer roomId, Integer semesterId);


    int countByCourse_Department_DepartmentIdAndSemester_IdIn(Integer departmentId, List<Integer> semesterIds);

    @Query("SELECT COUNT(c) > 0 FROM ClassEntity c WHERE c.course.department.departmentId = :departmentId AND c.semester.id IN :semesterIds")
    boolean existsByCourse_Department_DepartmentIdAndSemester_IdIn(@Param("departmentId") Integer departmentId, @Param("semesterIds") List<Integer> semesterIds);


    @Query("SELECT COUNT(c) > 0 FROM ClassEntity c WHERE c.course.id = :courseId AND c.semester.id IN :semesterIds")
    boolean existsByCourse_IdAndSemester_IdIn(@Param("courseId") Integer courseId, @Param("semesterIds") List<Integer> semesterIds);
}