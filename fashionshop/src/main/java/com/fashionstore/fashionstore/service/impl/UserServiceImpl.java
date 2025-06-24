package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.User;
import com.fashionstore.fashionstore.repository.UserRepository;
import com.fashionstore.fashionstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Integer id, User user) {
        user.setId(id);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    // Đăng ký user mới
    @Override
    public User registerUser(User user) {
        // if (userRepository.existsByUsername(user.getUsername())) {
        // throw new RuntimeException("Username already exists");
        // }
        // if (userRepository.existsByEmail(user.getEmail())) {
        // throw new RuntimeException("Email already exists");
        // }
        // user.setRole("CUSTOMER");
        // user.setStatus(true);
        // Lưu thẳng password (không mã hóa)
        return userRepository.save(user);
    }

    // Đăng nhập bằng username và password thô
    @Override
    public Optional<User> login(String username, String rawPassword) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(rawPassword)) {
            return userOpt;
        }
        return Optional.empty();
    }

    // Đổi mật khẩu
    @Override
    public boolean changePassword(Integer userId, String oldPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(oldPassword)) {
            User user = userOpt.get();
            user.setPassword(newPassword);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // Tìm kiếm user (ví dụ đơn giản theo username/email)
    @Override
    public List<User> searchUsers(String username, String email, int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        // Giả định có method userRepository.searchUsers...
        return userRepository.searchUsers(username, email, pageable);
    }

    // Lấy user hiện tại (nếu có security context)
    @Override
    public Optional<User> getCurrentUser() {
        return Optional.empty();
    }
}
