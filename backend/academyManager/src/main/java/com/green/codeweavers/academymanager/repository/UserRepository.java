package com.green.codeweavers.academymanager.repository;

import com.green.codeweavers.academymanager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
