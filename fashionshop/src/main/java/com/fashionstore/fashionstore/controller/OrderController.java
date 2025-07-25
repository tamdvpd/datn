package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.dto.order.CreateOrderRequest;
import com.fashionstore.fashionstore.dto.order.OrderResponse;
import com.fashionstore.fashionstore.entity.OrderStatus;
import com.fashionstore.fashionstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin("*") // Cho phép frontend gọi từ domain khác
public class OrderController {

    private final OrderService orderService;

    // ✅ API 1: Lấy tất cả đơn hàng (không phân trang)
    @GetMapping
    @Operation(summary = "Lấy tất cả đơn hàng")
    public ResponseEntity<List<OrderResponse>> getAll() {
        return ResponseEntity.ok(
            orderService.getAllOrders(PageRequest.of(0, Integer.MAX_VALUE)).getContent()
        );
    }

    // ✅ API 2: Phân trang và lọc theo trạng thái, thời gian
    @GetMapping("/page")
    @Operation(summary = "Phân trang & lọc đơn hàng")
    public ResponseEntity<Page<OrderResponse>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ) {
        return ResponseEntity.ok(
            orderService.search(status, from, to, PageRequest.of(page, size))
        );
    }

    // ✅ API 3: Lấy đơn hàng theo ID
    @GetMapping("/{id}")
    @Operation(summary = "Lấy đơn hàng theo ID")
    public ResponseEntity<OrderResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    // ✅ API 4: Tạo đơn hàng mới
    @PostMapping
    @Operation(summary = "Tạo mới đơn hàng")
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.create(request));
    }

    // ✅ API 5: Cập nhật thông tin đơn hàng
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin đơn hàng")
    public ResponseEntity<OrderResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.update(id, request));
    }

    // ✅ API 6: Xoá đơn hàng
    @DeleteMapping("/{id}")
    @Operation(summary = "Xoá đơn hàng")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ API 7: Cập nhật trạng thái đơn hàng
    @PutMapping("/{id}/status")
    @Operation(summary = "Cập nhật trạng thái đơn hàng")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, String> payload) {

        String statusStr = payload.get("status");
        String note = payload.get("note");
        Long userId = payload.containsKey("userId")
                ? Long.parseLong(payload.get("userId"))
                : null;

        OrderStatus status = OrderStatus.valueOf(statusStr);
        return ResponseEntity.ok(orderService.updateStatus(id, status, userId, note));
    }

    // ✅ API 8: Cập nhật mã vận đơn
    @PutMapping("/{id}/tracking")
    @Operation(summary = "Cập nhật mã vận đơn")
    public ResponseEntity<OrderResponse> updateTrackingCode(
            @PathVariable Integer id,
            @RequestBody Map<String, String> payload) {

        String trackingCode = payload.get("trackingCode");
        return ResponseEntity.ok(orderService.updateTrackingCode(id, trackingCode));
    }
}
