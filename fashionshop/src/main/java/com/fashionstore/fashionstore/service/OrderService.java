package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    // ======================================
    // ✅ 1. LẤY DANH SÁCH ĐƠN HÀNG (ADMIN)
    // ======================================

    // Lấy toàn bộ đơn hàng (không phân trang)
    List<Order> getAllOrders();

    // Lấy đơn hàng có phân trang
    Page<Order> getAllOrders(Pageable pageable);

    // Lọc đơn hàng theo trạng thái (phân trang)
    Page<Order> getOrdersByStatus(String status, Pageable pageable);

    // ======================================
    // ✅ 2. LẤY ĐƠN HÀNG THEO NGƯỜI DÙNG (USER)
    // ======================================

    // Lấy toàn bộ đơn hàng theo email người dùng
    List<Order> getOrdersByUserEmail(String email);

    // Lấy đơn hàng theo email + trạng thái
    List<Order> getOrdersByUserEmailAndStatus(String email, String status);

    // Lấy 5 đơn hàng gần nhất của user
    List<Order> getRecentOrdersByEmail(String email);

    // ======================================
    // ✅ 3. ĐƠN LẺ - GET / CREATE / UPDATE / DELETE
    // ======================================

    // Lấy đơn theo ID (admin hoặc user sở hữu)
    Optional<Order> getOrderById(Integer id);

    // Tạo đơn hàng mới (tự động tính tiền)
    Order createOrder(Order order);

    // Cập nhật đơn hàng (admin)
    Order updateOrder(Integer id, Order order);

    // Cập nhật trạng thái đơn hàng
    Optional<Order> updateOrderStatus(Integer id, String newStatus);

    // Xóa đơn hàng
    void deleteOrder(Integer id);

    // Kiểm tra đơn hàng có tồn tại không
    boolean existsById(Integer id);
}