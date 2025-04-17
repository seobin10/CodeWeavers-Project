package com.cw.cwu.repository;

import com.cw.cwu.domain.ClassEntity;
import com.cw.cwu.domain.EvaluationLecture;
import com.cw.cwu.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EvaluationLectureRepository extends JpaRepository<EvaluationLecture, Integer> {
    Optional<EvaluationLecture> findByStudentIdAndClassId(User student, ClassEntity classEntity);
}
