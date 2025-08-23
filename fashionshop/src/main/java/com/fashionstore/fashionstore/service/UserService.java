package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    Page<User> getAllUsers(Pageable pageable);

    Optional<User> getUserById(Integer id);

    User createUser(User user);

    User updateUser(Integer id, User user);

    void deleteUser(Integer id);

    User registerUser(User user);

    Optional<User> login(String email, String password);

    boolean changePassword(Integer userId, String oldPassword, String newPassword);

    List<User> searchUsers(String fullName, String email, int page, int size);

    Optional<User> getCurrentUser();

    // ------ Bổ sung thêm -------

    boolean existsByEmail(String email);

    long countUsers();

    Optional<User> findByEmail(String email);

    Page<User> searchUsersPage(String q, String role, Boolean status, Pageable pageable);

    User createAdminUser(String email, String fullName, String rawPassword, String role,
            Boolean status, String phoneNumber, String address, String imageUrl);

}
