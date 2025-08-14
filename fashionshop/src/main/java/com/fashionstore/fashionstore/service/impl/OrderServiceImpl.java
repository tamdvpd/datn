package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.Order;
import com.fashionstore.fashionstore.entity.OrderDetail;
import com.fashionstore.fashionstore.repository.OrderRepository;
import com.fashionstore.fashionstore.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    // ========================================
    // ✅ Lấy tất cả đơn hàng
    // ========================================
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    // ========================================
    // ✅ USER: Lấy đơn hàng theo email
    // ========================================
    @Override
    public List<Order> getOrdersByUserEmail(String email) {
        return orderRepository.findByUser_Email(email);
    }

    @Override
    public List<Order> getOrdersByUserEmailAndStatus(String email, String status) {
        return orderRepository.findByUser_EmailAndStatus(email, status);
    }

    @Override
    public Optional<Order> getOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return orderRepository.existsById(id);
    }

    @Override
    public List<Order> getRecentOrdersByEmail(String email) {
        return orderRepository.findTop5ByUser_EmailOrderByCreatedAtDesc(email);
    }

    @Override
    public Page<Order> getOrdersByStatus(String status, Pageable pageable) {
        return orderRepository.findByStatus(status, pageable);
    }

    // ========================================
    // ✅ Tạo đơn hàng mới
    // ========================================
    @Override
    @Transactional
    public Order createOrder(Order order) {
        order.setId(null);
        LocalDateTime now = LocalDateTime.now();
        order.setCreatedAt(now);
        order.setUpdatedAt(now);

        if (order.getStatus() == null) {
            order.setStatus("PENDING");
        }

        BigDecimal total = BigDecimal.ZERO;
        if (order.getOrderDetails() != null) {
            for (OrderDetail detail : order.getOrderDetails()) {
                detail.setOrder(order); // liên kết ngược
                detail.setCreatedAt(now);
                BigDecimal lineTotal = detail.getUnitPrice()
                        .multiply(BigDecimal.valueOf(detail.getQuantity()));
                total = total.add(lineTotal);
            }
        }
        order.setTotalAmount(total);

        // Thiết lập mặc định nếu không có
        if (order.getShippingFee() == null) {
            order.setShippingFee(BigDecimal.valueOf(30000));
        }
        if (order.getDiscountAmount() == null) {
            order.setDiscountAmount(BigDecimal.ZERO);
        }

        return orderRepository.save(order);
    }

    // ========================================
    // ✅ Cập nhật thông tin đơn hàng (Admin)
    // ========================================
    @Override
    @Transactional
    public Order updateOrder(Integer id, Order orderUpdate) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setUpdatedAt(LocalDateTime.now());
        order.setReceiverName(orderUpdate.getReceiverName());
        order.setReceiverPhone(orderUpdate.getReceiverPhone());
        order.setReceiverAddress(orderUpdate.getReceiverAddress());
        order.setNote(orderUpdate.getNote());
        order.setShippingProvider(orderUpdate.getShippingProvider());
        order.setPaymentMethod(orderUpdate.getPaymentMethod());
        order.setCoupon(orderUpdate.getCoupon());
        order.setShippingFee(orderUpdate.getShippingFee());
        order.setDiscountAmount(orderUpdate.getDiscountAmount());

        return orderRepository.save(order);
    }

    // ========================================
    // ✅ Cập nhật trạng thái đơn hàng
    // ========================================
    @Override
    @Transactional
    public Optional<Order> updateOrderStatus(Integer id, String status) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            String currentStatus = order.getStatus();

            List<String> allowedNextStatuses = switch (currentStatus) {
                case "PENDING" -> List.of("CONFIRMED", "CANCELLED");
                case "CONFIRMED" -> List.of("PROCESSING");
                case "PROCESSING" -> List.of("SHIPPED");
                case "SHIPPED" -> List.of("DELIVERED");
                case "DELIVERED" -> List.of("COMPLETED");
                case "COMPLETED", "CANCELLED" -> List.of(); // locked trạng thái
                default -> List.of();
            };
            if (!allowedNextStatuses.contains(status)) {
                throw new IllegalStateException("❌ Không thể chuyển từ " + currentStatus + " sang " + status);
            }

            order.setStatus(status);
            order.setUpdatedAt(LocalDateTime.now());
            return Optional.of(orderRepository.save(order));
        }
        return Optional.empty();
    }

    // ========================================
    // ✅ Xóa đơn hàng
    // ========================================
    @Override
    @Transactional
    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }
}