package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.Order;
import com.fashionstore.fashionstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*") // Cho phép frontend gọi từ domain khác
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // ====================================
    // ✅ 1. ADMIN - Lấy tất cả đơn hàng (không phân trang)
    // ====================================
    @GetMapping("/admin")
    public ResponseEntity<List<Order>> getAllOrdersForAdmin() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // ====================================
    // ✅ 2. ADMIN - Lấy đơn hàng có phân trang
    // ====================================
    @GetMapping("/admin/page")
    public ResponseEntity<Page<Order>> getAllOrdersPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    // ====================================
    // ✅ 3. USER - Lấy đơn hàng theo email + trạng thái (nếu có)
    // ====================================
    @GetMapping("/user")
    public ResponseEntity<List<Order>> getOrdersByUserEmailAndStatus(
            @RequestParam String email,
            @RequestParam(required = false) String status) {
        List<Order> orders = (status != null && !status.isBlank())
                ? orderService.getOrdersByUserEmailAndStatus(email, status)
                : orderService.getOrdersByUserEmail(email);

        return ResponseEntity.ok(orders);
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

        if (admin || (email != null && email.equalsIgnoreCase(orderEmail))) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // ====================================
    // ✅ 5. Tạo đơn hàng (tự động tính tiền, không cần DTO)
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

    // ====================================
    // ✅ 8. Xóa đơn hàng (ADMIN)
    // ====================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "false") boolean admin) {
        if (!admin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (!orderService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}