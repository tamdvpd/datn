package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.User;
import com.fashionstore.fashionstore.repository.UserRepository;
import com.fashionstore.fashionstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // TIÊM PasswordEncoder chính xác

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
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Mã hóa mật khẩu
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Integer id, User user) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existing.setFullName(user.getFullName());
        existing.setImageUrl(user.getImageUrl());
        existing.setAddress(user.getAddress());
        existing.setPhoneNumber(user.getPhoneNumber());
        existing.setStatus(user.getStatus());
        existing.setRole(user.getRole());

        return userRepository.save(existing);
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public User registerUser(User user) {
<<<<<<< Updated upstream
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        user.setRole("USER");
        user.setStatus(true);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Mã hóa mật khẩu
        return userRepository.save(user);
    }

    @Override
public Optional<User> login(String email, String password) {
    Optional<User> userOpt = userRepository.findByEmail(email);
    
    if (userOpt.isPresent()) {
        User user = userOpt.get();

        if (Boolean.FALSE.equals(user.getStatus())) {
            // Vẫn trả Optional để Controller biết tài khoản bị khóa
            return Optional.of(user); 
        }
=======
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
    // @Override
    // public Optional<User> login(String username, String rawPassword) {
    // Optional<User> userOpt = userRepository.findByUsername(username);
    // if (userOpt.isPresent() && userOpt.get().getPassword().equals(rawPassword)) {
    // return userOpt;
    // }
    // return Optional.empty();
    // }
>>>>>>> Stashed changes

        if (passwordEncoder.matches(password, user.getPassword())) {
            return Optional.of(user);
        }
    }
    
    return Optional.empty();
}


<<<<<<< Updated upstream
    @Override
    public boolean changePassword(Integer id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    @Override
    public List<User> searchUsers(String fullName, String email, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable).getContent()
                .stream()
                .filter(u -> (fullName == null || u.getFullName().toLowerCase().contains(fullName.toLowerCase())) &&
                        (email == null || u.getEmail().toLowerCase().contains(email.toLowerCase())))
                .toList();
    }
=======
    // // Tìm kiếm user (ví dụ đơn giản theo username/email)
    // @Override
    // public List<User> searchUsers(String username, String email, int page, int
    // size) {
    // Pageable pageable = Pageable.ofSize(size).withPage(page);
    // // Giả định có method userRepository.searchUsers...
    // return userRepository.searchUsers(username, email, pageable);
    // }
>>>>>>> Stashed changes

    @Override
    public Optional<User> getCurrentUser() {
        return Optional.empty(); // Để trống, cần tích hợp Spring Security để lấy user từ token
    }

    @Override
<<<<<<< Updated upstream
    public long countUsers() {
        return userRepository.count();
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

=======
    public Optional<User> login(String username, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }

    @Override
    public List<User> searchUsers(String username, String email, int page, int size) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchUsers'");
    }
>>>>>>> Stashed changes
}
