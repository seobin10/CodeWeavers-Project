package com.cw.cwu.repository;

import com.cw.cwu.domain.ReturnRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReturnRequestRepository extends JpaRepository<ReturnRequest, Integer> {
    List<ReturnRequest> findAllByStudent_UserId(String studentId);
}
