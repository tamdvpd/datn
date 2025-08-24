package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderDetailService {

    List<OrderDetail> getAllOrderDetails();

    Page<OrderDetail> getAllOrderDetails(Pageable pageable);

    // Lấy chi tiết đơn hàng theo ID
    Optional<OrderDetail> getOrderDetailById(Integer id);

    // Lấy danh sách chi tiết đơn hàng theo orderId (gắn với 1 đơn cụ thể)
    List<OrderDetail> getOrderDetailsByOrderId(Integer orderId);

    // ======================================
    // ✅ 2. TẠO / CẬP NHẬT / XOÁ CHI TIẾT
    // ======================================

    // Tạo mới chi tiết đơn hàng
    OrderDetail createOrderDetail(OrderDetail orderDetail);

    // Cập nhật chi tiết đơn hàng
    OrderDetail updateOrderDetail(Integer id, OrderDetail orderDetail);

    // Xoá chi tiết đơn hàng theo ID
    void deleteOrderDetail(Integer id);

    // Kiểm tra tồn tại theo ID
    boolean existsById(Integer id);
}