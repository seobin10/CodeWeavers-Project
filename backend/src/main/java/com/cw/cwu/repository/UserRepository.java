package com.cw.cwu.repository;

import com.cw.cwu.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
        Optional<User> findByUserId(String userId);
}


