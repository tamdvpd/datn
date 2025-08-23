package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

       boolean existsByEmail(String email);

       Optional<User> findByEmail(String email);

       @Query("""
                       SELECT u FROM User u
                       WHERE (:q IS NULL OR :q = '' OR
                              LOWER(u.fullName) LIKE LOWER(CONCAT('%', :q, '%')) OR
                              LOWER(u.email)    LIKE LOWER(CONCAT('%', :q, '%')))
                         AND (:role IS NULL OR :role = '' OR u.role = :role)
                         AND (:status IS NULL OR u.status = :status)
                     """)
       Page<User> searchUsersPage(@Param("q") String q,
                     @Param("role") String role,
                     @Param("status") Boolean status,
                     Pageable pageable);
}
