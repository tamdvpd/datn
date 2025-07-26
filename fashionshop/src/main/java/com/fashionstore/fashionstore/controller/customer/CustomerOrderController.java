package com.fashionstore.fashionstore.controller.customer;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/customer/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@CrossOrigin("*")
public class CustomerOrderController {

    private final OrderService orderService;

    // ✅ (Giữ nguyên nếu bạn muốn lấy toàn bộ, KHÔNG phân trang)
    @GetMapping
    @Operation(summary = "Lấy tất cả đơn hàng của người dùng (không phân trang)")
    public ResponseEntity<List<OrderResponse>> getAll(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(orderService.getOrdersOfUser(principal.getName()));
    }

    // ✅ MỚI: Lấy đơn hàng có phân trang + lọc trạng thái
    @GetMapping("/page")
    @Operation(summary = "Phân trang & lọc đơn hàng của người dùng hiện tại")
    public ResponseEntity<Page<OrderResponse>> getMyOrdersPaged(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) OrderStatus status
    ) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(
                orderService.getOrdersOfUser(principal.getName(), status, PageRequest.of(page, size))
        );
    }

    // ✅ Xem chi tiết 1 đơn hàng của tôi
    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết đơn hàng của người dùng")
    public ResponseEntity<OrderResponse> getById(@PathVariable Integer id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(orderService.getOrderForUser(id, principal.getName()));
    }

    // ✅ Tạo đơn hàng (user hiện tại)
    @PostMapping
    @Operation(summary = "Tạo đơn hàng mới (user hiện tại)")
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(orderService.createByUser(request, principal.getName()));
    }

    // ✅ Cập nhật đơn hàng (user hiện tại)
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin đơn hàng của tôi")
    public ResponseEntity<OrderResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CreateOrderRequest request,
            Principal principal
    ) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(orderService.updateByUser(id, request, principal.getName()));
    }

    // ✅ Huỷ đơn hàng của tôi (DELETE)
    @DeleteMapping("/{id}")
    @Operation(summary = "Huỷ đơn hàng của tôi (nếu PENDING)")
    public ResponseEntity<Void> delete(@PathVariable Integer id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        orderService.deleteByUser(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
