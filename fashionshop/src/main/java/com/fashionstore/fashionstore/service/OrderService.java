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

    // ✅ Lấy danh sách đơn hàng nội bộ (entity)
    List<Order> getAllOrders();

    // ✅ Phân trang đơn hàng (DTO)
    Page<OrderResponse> getAllOrders(Pageable pageable);

    // ✅ Lấy đơn hàng chi tiết theo ID (DTO)
    OrderResponse getById(Integer id);

    // ✅ Tạo đơn hàng từ request DTO
    OrderResponse create(CreateOrderRequest request);

    // ✅ Cập nhật đơn hàng từ request DTO
    OrderResponse update(Integer id, CreateOrderRequest request);

    // ✅ Xoá đơn hàng
    void delete(Integer id);

    // ✅ Cập nhật trạng thái đơn hàng
    OrderResponse updateStatus(Integer id, OrderStatus status, Long changedBy, String note);

    // ✅ Cập nhật mã vận đơn
    OrderResponse updateTrackingCode(Integer id, String trackingCode);

    // ✅ Tìm kiếm theo trạng thái và thời gian
    Page<OrderResponse> search(OrderStatus status, LocalDate from, LocalDate to, Pageable pageable);
}
