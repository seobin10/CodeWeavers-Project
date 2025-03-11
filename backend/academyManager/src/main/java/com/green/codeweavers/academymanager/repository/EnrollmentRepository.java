package com.green.codeweavers.academymanager.repository;

import com.green.codeweavers.academymanager.domain.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
}
