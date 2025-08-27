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

       // Đếm user đăng ký hôm nay
       @Query(value = """
                     SELECT COUNT(*)
                     FROM Users
                     WHERE CAST(created_at AS DATE) = CAST(GETDATE() AS DATE)
                     """, nativeQuery = true)
       long countUsersToday();

       // Đếm user đăng ký trong tuần hiện tại
       @Query(value = """
                     SELECT COUNT(*)
                     FROM Users
                     WHERE DATEPART(ISO_WEEK, created_at) = DATEPART(ISO_WEEK, GETDATE())
                       AND YEAR(created_at) = YEAR(GETDATE())
                     """, nativeQuery = true)
       long countUsersThisWeek();

       // Đếm user đăng ký trong tháng hiện tại
       @Query(value = """
                     SELECT COUNT(*)
                     FROM Users
                     WHERE MONTH(created_at) = MONTH(GETDATE())
                       AND YEAR(created_at) = YEAR(GETDATE())
                     """, nativeQuery = true)
       long countUsersThisMonth();

       // Đếm user đăng ký trong năm hiện tại
       @Query(value = """
                     SELECT COUNT(*)
                     FROM Users
                     WHERE YEAR(created_at) = YEAR(GETDATE())
                     """, nativeQuery = true)
       long countUsersThisYear();

       // Đếm tất cả tài khoản
       @Query(value = "SELECT COUNT(*) FROM Users", nativeQuery = true)
       long countAllAccounts();

       // Đếm tài khoản role = 'USER'
       @Query(value = "SELECT COUNT(*) FROM Users WHERE role = 'USER'", nativeQuery = true)
       long countUserAccounts();

       // Đếm tài khoản role = 'ADMIN'
       @Query(value = "SELECT COUNT(*) FROM Users WHERE role = 'ADMIN'", nativeQuery = true)
       long countAdminAccounts();

       // Đếm tài khoản đang hoạt động (status = 1)
       @Query(value = "SELECT COUNT(*) FROM Users WHERE status = 1", nativeQuery = true)
       long countActiveAccounts();

       // Đếm tài khoản bị khóa (status = 0)
       @Query(value = "SELECT COUNT(*) FROM Users WHERE status = 0", nativeQuery = true)
       long countInactiveAccounts();
}
