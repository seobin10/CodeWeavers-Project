package com.cw.cwu.repository;

import com.cw.cwu.domain.Department;
import com.cw.cwu.domain.Semester;
import com.cw.cwu.domain.StudentRecord;
import com.cw.cwu.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRecordRepository extends JpaRepository<StudentRecord, Long> {

    Optional<StudentRecord> findByStudentAndSemester(User student, Semester semester);

    boolean existsBySemesterAndStudent_Department(Semester semester, Department department);

    List<StudentRecord> findByStudent(User student);
}

