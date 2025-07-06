package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.User;
import com.fashionstore.fashionstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @RequestMapping({ "", "/" })
    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Integer id) {
        return ResponseEntity.of(userService.getUserById(id));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User createdUser = userService.registerUser(user);
            return ResponseEntity.ok(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Registration failed: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Server error: " + e.getMessage()));
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Thiếu email hoặc mật khẩu."));
        }

        Optional<User> userOpt = userService.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Email hoặc mật khẩu không chính xác."));
        }

        User user = userOpt.get();

        if (Boolean.FALSE.equals(user.getStatus())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("Tài khoản đã bị khóa, vui lòng liên hệ quản trị viên."));
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Email hoặc mật khẩu không chính xác."));
        }

        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Integer id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Integer id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        boolean changed = userService.changePassword(id, oldPassword, newPassword);

        if (changed) {
            return ResponseEntity.ok("Đổi mật khẩu thành công");
        } else {
            return ResponseEntity.badRequest().body("Mật khẩu hiện tại không chính xác");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.searchUsers(fullName, email, page, size));
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getCurrentUser() {
        Optional<User> userOpt = userService.getCurrentUser();
        return ResponseEntity.of(userOpt);
    }

    // API check trùng email, hỗ trợ frontend
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateUserInfo(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> updates) {
        try {
            User user = userService.getUserById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Cập nhật họ tên
            if (updates.containsKey("fullName") && updates.get("fullName") instanceof String)
                user.setFullName((String) updates.get("fullName"));

            // Cập nhật địa chỉ
            if (updates.containsKey("address") && updates.get("address") instanceof String)
                user.setAddress((String) updates.get("address"));

            // Cập nhật số điện thoại
            if (updates.containsKey("phoneNumber") && updates.get("phoneNumber") instanceof String)
                user.setPhoneNumber((String) updates.get("phoneNumber"));

            // Cập nhật avatar
            if (updates.containsKey("imageUrl") && updates.get("imageUrl") instanceof String) {
                String imageUrl = (String) updates.get("imageUrl");

                // Kiểm tra nếu là base64, có thể từ chối hoặc xử lý riêng
                if (imageUrl.startsWith("data:image/")) {
                    throw new RuntimeException(
                            "Không hỗ trợ lưu ảnh dạng base64 vào database. Vui lòng upload ảnh trước.");
                }

                user.setImageUrl(imageUrl);
            }

            // Cập nhật trạng thái
            if (updates.containsKey("status") && updates.get("status") != null) {
                Object statusObj = updates.get("status");
                if (statusObj instanceof Boolean) {
                    user.setStatus((Boolean) statusObj);
                } else {
                    user.setStatus(Boolean.parseBoolean(statusObj.toString()));
                }
            }

            // Cập nhật provider nếu cần
            if (updates.containsKey("provider") && updates.get("provider") instanceof String)
                user.setProvider((String) updates.get("provider"));

            // Không cho phép sửa role từ phía user nếu không cần thiết
            if (updates.containsKey("role")) {
                // Bỏ qua hoặc cho phép cập nhật nếu cần
                // user.setRole((String) updates.get("role"));
            }

            userService.updateUser(id, user);

            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Error: " + e.getMessage()));
        }
    }

}
