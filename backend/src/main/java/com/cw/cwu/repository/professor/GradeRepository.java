package com.cw.cwu.repository.professor;

import com.cw.cwu.domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Integer> {
}
