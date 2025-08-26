package com.fashionstore.fashionstore.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    private static final Map<String, String> constraintToFieldMap = Map.ofEntries(
            Map.entry("uq__coupons__code", "code"),
            Map.entry("uq__users__email", "email"),
            Map.entry("uq__users__phonenumber", "phoneNumber"),
            Map.entry("uq__suppliers__email", "email"),
            Map.entry("uq__suppliers__phonenumber", "phoneNumber"));

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        String message = ex.getMostSpecificCause().getMessage().toLowerCase();

        for (Map.Entry<String, String> entry : constraintToFieldMap.entrySet()) {
            if (message.contains(entry.getKey().toLowerCase())) {
                String field = entry.getValue();
                String friendlyMessage = switch (field) {
                    case "code" -> "Mã giảm giá đã tồn tại";
                    case "email" -> "Email đã được sử dụng";
                    case "phoneNumber" -> "Số điện thoại đã tồn tại";
                    default -> "Dữ liệu đã tồn tại";
                };
                errors.put(field, friendlyMessage);
            }
        }

        if (errors.isEmpty()) {
            errors.put("error", "Dữ liệu đã tồn tại hoặc vi phạm ràng buộc");
        }

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
    }

}