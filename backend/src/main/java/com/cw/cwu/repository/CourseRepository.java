package com.cw.cwu.repository;

import com.cw.cwu.domain.Course;
import com.cw.cwu.domain.CourseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    // 특정 학과에 속한 모든 과목 목록 + 교양 과목을 조회
    @Query("SELECT c FROM Course c WHERE c.department.id = :deptId OR c.type = :liberal")
    List<Course> findCoursesByDepartmentOrLiberal(@Param("deptId") Integer deptId,
                                                  @Param("liberal") CourseType liberal);

    int countByDepartment_DepartmentId(Integer departmentId);
}
