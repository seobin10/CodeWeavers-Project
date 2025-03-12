package com.green.codeweavers.academymanager.repository;

import com.green.codeweavers.academymanager.domain.Departments;
import com.green.codeweavers.academymanager.domain.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Departments, Integer> {
}
