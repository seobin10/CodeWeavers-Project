package com.cw.cwu.repository;

import com.cw.cwu.domain.Course;
import com.cw.cwu.domain.CourseStatus;
import com.cw.cwu.domain.CourseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    // 특정 학과에 속한 모든 과목 목록 + 교양 과목을 조회
    @Query("""
        SELECT c FROM Course c 
        WHERE c.status = :status 
        AND (c.department.id = :deptId OR c.type = :liberal)
        """)
    List<Course> findAvailableCoursesByDepartmentOrLiberal(@Param("deptId") Integer deptId,
                                                           @Param("liberal") CourseType liberal,
                                                           @Param("status") CourseStatus status);

    int countByDepartment_DepartmentId(Integer departmentId);

    // 특정 학과의 모든 과목 조회
    List<Course> findByDepartment_DepartmentId(Integer departmentId);

    // 공통 과목 (학과 미지정)
    List<Course> findByDepartmentIsNull();

    // 과목명 중복 체크
    boolean existsByName(String name);

}
