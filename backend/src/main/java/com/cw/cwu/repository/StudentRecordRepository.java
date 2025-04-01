package com.cw.cwu.repository;

import com.cw.cwu.domain.StudentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRecordRepository extends JpaRepository<StudentRecord, Long> {

    // 학생의 총 취득 학점(SUM(earned)) 조회
    @Query("SELECT SUM(sr.earned) FROM StudentRecord sr WHERE sr.student.userId = :studentId")
    Integer findTotalEarnedCreditsByStudent(@Param("studentId") String studentId);

}

