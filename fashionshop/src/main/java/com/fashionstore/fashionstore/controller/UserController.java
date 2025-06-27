package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.User;
import com.fashionstore.fashionstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Integer id) {
        return ResponseEntity.of(userService.getUserById(id));
    }

    @PostMapping("/register")
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

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
    try {
        Optional<User> userOpt = userService.login(email, password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(null);  // Ẩn password khi trả về frontend
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid email or password"));
        }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Server error: " + e.getMessage()));
    }
}


    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Integer id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Integer id,
                                               @RequestParam String oldPassword,
                                               @RequestParam String newPassword) {
        boolean changed = userService.changePassword(id, oldPassword, newPassword);
        return changed ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
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
    @GetMapping("/{id}/update")
public ResponseEntity<?> updateUserInfo(
        @PathVariable Integer id,
        @RequestParam(required = false) String fullName,
        @RequestParam(required = false) String role,
        @RequestParam(required = false) String address,
        @RequestParam(required = false) String phoneNumber,
        @RequestParam(required = false) Boolean status
) {
    try {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (fullName != null) user.setFullName(fullName);
        if (role != null) user.setRole(role);
        if (address != null) user.setAddress(address);
        if (phoneNumber != null) user.setPhoneNumber(phoneNumber);
        if (status != null) user.setStatus(status);

        userService.updateUser(id, user);
        
        return ResponseEntity.ok("User updated successfully");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Error: " + e.getMessage()));
    }
}

}
