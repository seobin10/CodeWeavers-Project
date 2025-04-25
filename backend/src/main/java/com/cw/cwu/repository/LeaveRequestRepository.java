package com.cw.cwu.repository;

import com.cw.cwu.domain.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {
    List<LeaveRequest> findAllByStudent_UserId(String studentId);
}
