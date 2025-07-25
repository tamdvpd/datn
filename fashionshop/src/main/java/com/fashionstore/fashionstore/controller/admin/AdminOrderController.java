package com.fashionstore.fashionstore.controller.admin;

import com.fashionstore.fashionstore.dto.order.CreateOrderRequest;
import com.fashionstore.fashionstore.dto.order.OrderResponse;
import com.fashionstore.fashionstore.entity.OrderStatus;
import com.fashionstore.fashionstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin("*")
public class AdminOrderController {

    private final OrderService orderService;

    // ✅ API 1: Lấy tất cả đơn hàng (không phân trang)
    @GetMapping
    @Operation(summary = "Lấy toàn bộ đơn hàng (không phân trang)")
    public ResponseEntity<List<OrderResponse>> getAll() {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        return ResponseEntity.ok(orderService.getAllOrders(pageable).getContent());
    }

    // ✅ API 2: Phân trang và lọc đơn hàng
    @GetMapping("/")
    @Operation(summary = "Phân trang và lọc đơn hàng theo trạng thái và khoảng thời gian")
    public ResponseEntity<Page<OrderResponse>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(orderService.search(status, from, to, pageable));
    }

    // ✅ API 3: Lấy chi tiết đơn hàng theo ID
    @GetMapping("/{id}")
    @Operation(summary = "Xem chi tiết đơn hàng")
    public ResponseEntity<OrderResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    // ✅ API 4: Tạo đơn hàng mới từ admin
    @PostMapping
    @Operation(summary = "Tạo đơn hàng (admin)")
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.create(request));
    }

    // ✅ API 5: Cập nhật đơn hàng
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin đơn hàng")
    public ResponseEntity<OrderResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CreateOrderRequest request
    ) {
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
            @RequestBody Map<String, String> payload
    ) {
        String statusStr = payload.get("status");
        String note = payload.get("note");
        Long userId = payload.containsKey("userId") ? Long.parseLong(payload.get("userId")) : null;

        OrderStatus status = OrderStatus.valueOf(statusStr);
        return ResponseEntity.ok(orderService.updateStatus(id, status, userId, note));
    }

    // ✅ API 8: Cập nhật mã vận đơn
    @PutMapping("/{id}/tracking")
    @Operation(summary = "Cập nhật mã vận đơn")
    public ResponseEntity<OrderResponse> updateTrackingCode(
            @PathVariable Integer id,
            @RequestBody Map<String, String> payload
    ) {
        String trackingCode = payload.get("trackingCode");
        return ResponseEntity.ok(orderService.updateTrackingCode(id, trackingCode));
    }
}
