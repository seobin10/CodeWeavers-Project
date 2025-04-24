package com.cw.cwu.repository;

import com.cw.cwu.domain.Department;
import com.cw.cwu.domain.DepartmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    boolean existsByDepartmentName(String departmentName);

    List<Department> findByStatus(DepartmentStatus status);
}
