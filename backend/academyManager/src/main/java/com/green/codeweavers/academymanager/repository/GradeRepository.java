package com.green.codeweavers.academymanager.repository;


import com.green.codeweavers.academymanager.domain.Grades;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grades, Integer> {
}
