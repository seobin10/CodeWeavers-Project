package com.cw.cwu.repository;

import com.cw.cwu.domain.Semester;
import com.cw.cwu.domain.StudentRecord;
import com.cw.cwu.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRecordRepository extends JpaRepository<StudentRecord, Long> {


    boolean existsByStudentAndSemester(User student, Semester semester);
}

