package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.Order;
import com.fashionstore.fashionstore.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

<<<<<<< Updated upstream
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE " +
           "(:fullName IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :fullName, '%'))) AND " +
           "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))")
    List<User> searchUsers(@Param("fullName") String fullName,
                           @Param("email") String email,
                           Pageable pageable);

    Optional<Order> findById(Long userId);
=======
       // boolean existsByUsername(String username);

       boolean existsByEmail(String email);

       // Optional<User> findByUsername(String username);

       // @Query("SELECT u FROM User u WHERE " +
       // "(:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username,
       // '%'))) AND " +
       // "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))")
       // List<User> searchUsers(@Param("username") String username,
       // @Param("email") String email,
       // Pageable pageable);
>>>>>>> Stashed changes
}
