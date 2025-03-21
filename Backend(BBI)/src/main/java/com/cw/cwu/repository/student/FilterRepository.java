package com.cw.cwu.repository.student;

import com.cw.cwu.domain.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilterRepository extends JpaRepository<ClassEntity, Integer> {

    @Query("SELECT DISTINCT d.departmentName FROM Department d")
    List<String> findDistinctDepartments();

    @Query("SELECT DISTINCT c.type FROM Course c")
    List<String> findDistinctCourseTypes();

    @Query("SELECT DISTINCT c.year FROM Course c ORDER BY c.year")
    List<Integer> findDistinctCourseYears();

    @Query("SELECT DISTINCT cl.day FROM ClassEntity cl")
    List<String> findDistinctClassDays();

    @Query("SELECT DISTINCT cl.startTime FROM ClassEntity cl ORDER BY cl.startTime")
    List<Integer> findDistinctClassTimes();

    @Query("SELECT DISTINCT c.credit FROM Course c ORDER BY c.credit")
    List<Integer> findDistinctCredits();
}
