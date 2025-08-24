package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.OrderDetail;
import com.fashionstore.fashionstore.repository.OrderDetailRepository;
import com.fashionstore.fashionstore.service.OrderDetailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    // ============================================
    // ✅ 1. Lấy tất cả chi tiết đơn hàng (không phân trang)
    // ============================================
    @Override
    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }

    // ============================================
    // ✅ 2. Lấy chi tiết đơn hàng có phân trang
    // ============================================
    @Override
    public Page<OrderDetail> getAllOrderDetails(Pageable pageable) {
        return orderDetailRepository.findAll(pageable);
    }

    // ============================================
    // ✅ 3. Lấy chi tiết đơn hàng theo ID
    // ============================================
    @Override
    public Optional<OrderDetail> getOrderDetailById(Integer id) {
        return orderDetailRepository.findById(id);
    }

    // ============================================
    // ✅ 4. Tạo mới chi tiết đơn hàng
    // ============================================
    @Override
    @Transactional
    public OrderDetail createOrderDetail(OrderDetail orderDetail) {
        orderDetail.setId(null); // Đảm bảo tạo mới
        orderDetail.setCreatedAt(LocalDateTime.now());
        return orderDetailRepository.save(orderDetail);
    }

    // ============================================
    // ✅ 5. Cập nhật chi tiết đơn hàng
    // ============================================
    @Override
    @Transactional
    public OrderDetail updateOrderDetail(Integer id, OrderDetail updated) {
        OrderDetail existing = orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderDetail not found"));

        existing.setQuantity(updated.getQuantity());
        existing.setUnitPrice(updated.getUnitPrice());
        existing.setProductDetail(updated.getProductDetail());
        existing.setOrder(updated.getOrder());

        return orderDetailRepository.save(existing);
    }

    // ============================================
    // ✅ 6. Xóa chi tiết đơn hàng
    // ============================================
    @Override
    @Transactional
    public void deleteOrderDetail(Integer id) {
        orderDetailRepository.deleteById(id);
    }

    // ============================================
    // ✅ 7. Kiểm tra tồn tại theo ID
    // ============================================
    @Override
    public boolean existsById(Integer id) {
        return orderDetailRepository.existsById(id);
    }

    // ============================================
    // ✅ 8. Lấy chi tiết đơn hàng theo orderId
    // ============================================
    @Override
    public List<OrderDetail> getOrderDetailsByOrderId(Integer orderId) {
        return orderDetailRepository.findByOrder_Id(orderId);
    }
}