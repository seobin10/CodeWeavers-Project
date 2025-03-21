package com.cw.cwu.repository;

import com.cw.cwu.domain.Enrollment;
import com.cw.cwu.domain.User;
import com.cw.cwu.domain.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    // 특정 학생이 특정 강의를 신청했는지 확인 (중복 방지)
    Optional<Enrollment> findByStudentAndEnrolledClassEntity(User student, ClassEntity enrolledClassEntity);

    // 특정 강의의 현재 신청된 인원 수 조회 (정원 초과 방지)
    int countByEnrolledClassEntity(ClassEntity enrolledClassEntity);

    // 학생이 신청한 모든 수강 내역 조회
    List<Enrollment> findByStudent(User student);
}
