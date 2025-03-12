package com.green.codeweavers.academymanager.repository;

import com.green.codeweavers.academymanager.domain.Classes;
import com.green.codeweavers.academymanager.domain.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassesRepository extends JpaRepository<Classes, Integer> {
}
