package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.dto.order.CreateOrderRequest;
import com.fashionstore.fashionstore.dto.order.OrderResponse;
import com.fashionstore.fashionstore.entity.Order;
import com.fashionstore.fashionstore.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    // ===== ADMIN APIs =====

    // ✅ Lấy toàn bộ đơn hàng (không phân trang, entity)
    List<Order> getAllOrders();

    // ✅ Phân trang đơn hàng (trả về DTO)
    Page<OrderResponse> getAllOrders(Pageable pageable);

    // ✅ Lấy chi tiết đơn hàng theo ID
    OrderResponse getById(Integer id);

    // ✅ Tạo đơn hàng từ phía admin (userId truyền vào request)
    OrderResponse create(CreateOrderRequest request);

    // ✅ Cập nhật đơn hàng
    OrderResponse update(Integer id, CreateOrderRequest request);

    // ✅ Xoá đơn hàng theo ID
    void delete(Integer id);

    // ✅ Cập nhật trạng thái đơn hàng
    OrderResponse updateStatus(Integer id, OrderStatus status, Long changedBy, String note);

    // ✅ Cập nhật mã vận đơn
    OrderResponse updateTrackingCode(Integer id, String trackingCode);

    // ✅ Tìm kiếm đơn hàng theo trạng thái và khoảng thời gian
    Page<OrderResponse> search(OrderStatus status, LocalDate from, LocalDate to, Pageable pageable);

    // ===== CUSTOMER APIs =====

    // ✅ Tạo đơn hàng từ người dùng hiện tại (username lấy từ Spring Security)
    OrderResponse createByUser(CreateOrderRequest request, String username);

    // ✅ Cập nhật đơn hàng của người dùng hiện tại
    OrderResponse updateByUser(Integer id, CreateOrderRequest request, String username);

    // ✅ Xoá đơn hàng của người dùng hiện tại
    void deleteByUser(Integer id, String username);

    // ✅ Lấy tất cả đơn hàng của người dùng hiện tại
    List<OrderResponse> getOrdersOfUser(String username);

    // ✅ Lấy đơn hàng chi tiết theo ID (của người dùng hiện tại)
    OrderResponse getOrderForUser(Integer id, String username);

    // OrderService.java
    Page<OrderResponse> getOrdersOfUser(String username, OrderStatus status, Pageable pageable);

}
