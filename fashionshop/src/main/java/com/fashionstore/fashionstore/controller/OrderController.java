package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.Order;
import com.fashionstore.fashionstore.service.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // ====================================
    // ✅ 1. ADMIN - Lấy tất cả đơn hàng (PHÂN TRANG THỐNG NHẤT)
    // ====================================
    @GetMapping("/admin")
    public ResponseEntity<Page<Order>> getAllOrdersForAdminPaged(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction) {
        Pageable pageable = buildPageable(page, size, sortBy, direction);
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    // ====================================
    // ✅ 2. ADMIN - Lọc theo trạng thái (PHÂN TRANG THỐNG NHẤT)
    // ====================================
    @GetMapping("/admin/status")
    public ResponseEntity<Page<Order>> getOrdersByStatusForAdmin(
            @RequestParam String status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction) {
        Pageable pageable = buildPageable(page, size, sortBy, direction);
        return ResponseEntity.ok(orderService.getOrdersByStatus(status, pageable));
    }

    // ====================================
    // ✅ 3. USER - Lấy đơn hàng theo userId + (optional) status (PHÂN TRANG THỐNG
    // NHẤT)
    // ====================================
    @GetMapping("/user")
    public ResponseEntity<Page<Order>> getOrdersByUserIdAndStatus(
            @RequestParam Integer userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction) {
        Pageable pageable = buildPageable(page, size, sortBy, direction);
        Page<Order> result = (status != null && !status.isBlank())
                ? orderService.getOrdersByUserIdAndStatus(userId, status, pageable)
                : orderService.getOrdersByUserId(userId, pageable);
        return ResponseEntity.ok(result);
    }

    // ====================================
    // ✅ 4. GET BY ID (admin hoặc đúng người dùng)
    // ====================================
    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(
            @PathVariable Integer id,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "false") boolean admin) {
        Optional<Order> optionalOrder = orderService.getOrderById(id);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Order order = optionalOrder.get();
        String orderEmail = order.getUser().getEmail();

        boolean allowed = admin || (email != null && email.equalsIgnoreCase(orderEmail));
        if (!allowed) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Order full = orderService.getOrderByIdWithDetails(id).orElse(order);
        return ResponseEntity.ok(full);
    }

    // ====================================
    // ✅ 5. Tạo đơn hàng
    // ====================================
    @PostMapping
    public ResponseEntity<Order> create(@RequestBody Order order) {
        try {
            Order created = orderService.createOrder(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // ====================================
    // ✅ 6. Cập nhật đơn hàng (ADMIN)
    // ====================================
    @PutMapping("/{id}")
    public ResponseEntity<Order> update(
            @PathVariable Integer id,
            @RequestBody Order order,
            @RequestParam(defaultValue = "false") boolean admin) {
        if (!admin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            Order updated = orderService.updateOrder(id, order);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ====================================
    // ✅ 7. Cập nhật trạng thái đơn (ADMIN)
    // ====================================
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Integer id,
            @RequestParam String status,
            @RequestParam(defaultValue = "false") boolean admin) {
        if (!admin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền cập nhật trạng thái đơn hàng.");
        }
        try {
            Optional<Order> updated = orderService.updateOrderStatus(id, status);
            return updated.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id", "status", "totalAmount", "createdAt", "updatedAt");

    private Pageable buildPageable(Integer page, Integer size, String sortBy, String direction) {
        int safePage = (page == null || page < 0) ? 0 : page;
        int safeSize = (size == null || size < 1) ? 10 : Math.min(size, 100);

        String safeSortBy = (sortBy == null || !ALLOWED_SORT_FIELDS.contains(sortBy))
                ? "createdAt"
                : sortBy;

        Sort.Direction dir = ("asc".equalsIgnoreCase(direction)) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(safePage, safeSize, Sort.by(dir, safeSortBy));
    }

    // OrderController.java
    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelByUser(
            @PathVariable Integer id,
            @RequestParam String email) {
        var ok = orderService.cancelByUser(id, email);
        return ok ? ResponseEntity.ok("Đã huỷ đơn.")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể huỷ đơn ở trạng thái hiện tại.");
    }

    @PostMapping("/{id}/received")
    public ResponseEntity<?> userMarkReceived(
            @PathVariable Integer id,
            @RequestParam String email) {

        var opt = orderService.getOrderById(id);
        if (opt.isEmpty())
            return ResponseEntity.notFound().build();

        Order order = opt.get();
        if (!order.getUser().getEmail().equalsIgnoreCase(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Bạn không có quyền với đơn này.");
        }

        try {
            orderService.updateOrderStatus(id, "COMPLETED");
            return ResponseEntity.ok("Đơn hàng đã được xác nhận hoàn tất.");
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}