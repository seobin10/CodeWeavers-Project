package com.cw.cwu.repository.user;

import com.cw.cwu.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
        Optional<User> findByUserId(String userId);

        // JPQL을 사용하여 userName으로 userId만 조회하는 메서드
        @Query("select u.userId from User u where u.userName = :userName")
        Optional<String> findUserIdByUserName(@Param("userName") String userName);

        // 비밀번호 찾기(Optional 사용)
        @Query("select u.userPassword from User u where u.userId = :userId and u.userEmail = :userEmail")
        Optional<String> findUserPasswordByUserIdAndEmail(@Param("userId") String userId, @Param("userEmail") String userEmail);

        boolean existsByUserEmail(String userEmail);

        boolean existsByUserPhone(String userPhone);

        Page<User> findByUserIdContainingIgnoreCaseOrUserNameContainingIgnoreCase(
                String userId, String userName, Pageable pageable
        );
}


