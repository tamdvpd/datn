package com.fashionstore.fashionstore.exception;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    // Ánh xạ tên constraint từ DB -> tên field trong entity
    private static final Map<String, String> constraintToFieldMap = Map.of(
            "uq__coupons__code", "code",
            "uq__users__email", "email"
    // bạn có thể thêm các constraint khác tại đây
    );

    // Xử lý lỗi vi phạm UNIQUE constraint
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        String message = ex.getMostSpecificCause().getMessage().toLowerCase();

        for (Map.Entry<String, String> entry : constraintToFieldMap.entrySet()) {
            if (message.contains(entry.getKey())) {
                String field = entry.getValue();
                String friendlyMessage = switch (field) {
                    case "code" -> "Mã giảm giá đã tồn tại";
                    case "email" -> "Email đã được sử dụng";
                    default -> "Dữ liệu đã tồn tại";
                };
                errors.put(field, friendlyMessage);
                return ResponseEntity.badRequest().body(errors);
            }
        }

        errors.put("error", "Dữ liệu đã tồn tại hoặc vi phạm ràng buộc");
        return ResponseEntity.badRequest().body(errors);
    }
}
