package com.cw.cwu.repository.professor;

import com.cw.cwu.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
