package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.OrderDetail;
import com.fashionstore.fashionstore.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*") // Cho phép truy cập từ bất kỳ frontend nào
@RestController
@RequestMapping("/orderDetails")
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    // ================================
    // ✅ 1. Lấy tất cả chi tiết đơn hàng (Admin)
    // ================================
    @GetMapping
    public ResponseEntity<List<OrderDetail>> getAll() {
        List<OrderDetail> list = orderDetailService.getAllOrderDetails();
        return ResponseEntity.ok(list);
    }

    // ================================
    // ✅ 2. Lấy chi tiết đơn hàng theo ID
    // ================================
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetail> getById(@PathVariable Integer id) {
        return orderDetailService.getOrderDetailById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================================
    // ✅ 3. Lấy danh sách chi tiết theo ID đơn hàng
    // ================================
    @GetMapping("/byOrder/{orderId}")
    public ResponseEntity<List<OrderDetail>> getByOrderId(@PathVariable Integer orderId) {
        List<OrderDetail> details = orderDetailService.getOrderDetailsByOrderId(orderId);
        return ResponseEntity.ok(details);
    }

    // ================================
    // ✅ 4. Tạo mới chi tiết đơn hàng (Admin)
    // ================================
    @PostMapping
    public ResponseEntity<?> create(@RequestBody OrderDetail orderDetail) {
        try {
            OrderDetail created = orderDetailService.createOrderDetail(orderDetail);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Không thể tạo chi tiết đơn hàng: " + e.getMessage());
        }
    }

    // ================================
    // ✅ 5. Cập nhật chi tiết đơn hàng (Admin)
    // ================================
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody OrderDetail orderDetail) {
        try {
            OrderDetail updated = orderDetailService.updateOrderDetail(id, orderDetail);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy OrderDetail ID = " + id);
        }
    }

    // ================================
    // ✅ 6. Xóa chi tiết đơn hàng (Admin)
    // ================================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        if (!orderDetailService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chi tiết đơn hàng không tồn tại");
        }
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.noContent().build();
    }
}