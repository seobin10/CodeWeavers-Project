package com.cw.cwu.repository;

import com.cw.cwu.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    boolean existsByDepartmentName(String departmentName);
}
