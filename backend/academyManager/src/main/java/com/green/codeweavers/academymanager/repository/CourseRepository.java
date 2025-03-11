package com.green.codeweavers.academymanager.repository;

import com.green.codeweavers.academymanager.domain.Courses;
import com.green.codeweavers.academymanager.domain.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Courses, Integer> {
}
