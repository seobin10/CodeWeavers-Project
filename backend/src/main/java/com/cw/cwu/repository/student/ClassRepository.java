package com.cw.cwu.repository.student;

import com.cw.cwu.domain.ClassEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Integer> {

    Page<ClassEntity> findByProfessor_UserId(String userId, Pageable pageable);
}