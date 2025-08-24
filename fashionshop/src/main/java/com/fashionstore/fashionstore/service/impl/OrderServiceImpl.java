package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.Order;
import com.fashionstore.fashionstore.entity.OrderDetail;
import com.fashionstore.fashionstore.repository.OrderRepository;
import com.fashionstore.fashionstore.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
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
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Page<Order> getOrdersByStatus(String status, Pageable pageable) {
        return orderRepository.findByStatus(normalizeStatus(status), pageable);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Page<Order> getOrdersByUserId(Integer userId, Pageable pageable) {
        return orderRepository.findByUser_Id(userId, pageable);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Page<Order> getOrdersByUserIdAndStatus(Integer userId, String status, Pageable pageable) {
        return orderRepository.findByUser_IdAndStatus(userId, normalizeStatus(status), pageable);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Optional<Order> getOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Integer id, Order update) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + id));

        order.setReceiverName(update.getReceiverName());
        order.setReceiverPhone(update.getReceiverPhone());
        order.setReceiverAddress(update.getReceiverAddress());
        order.setNote(update.getNote());
        order.setShippingProvider(update.getShippingProvider());
        order.setPaymentMethod(update.getPaymentMethod());
        order.setCoupon(update.getCoupon());
        order.setShippingFee(update.getShippingFee());
        order.setDiscountAmount(update.getDiscountAmount());

        if (update.getOrderDetails() != null && !update.getOrderDetails().isEmpty()) {
            order.setOrderDetails(update.getOrderDetails());
        }

        autoCalculateTotals(order);
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> updateOrderStatus(Integer id, String newStatus) {
        return orderRepository.findById(id).map(order -> {
            String target = normalizeStatus(newStatus);
            String current = order.getStatus();

            List<String> allowedNextStatuses = switch (current) {
                case "Pending Confirmation" -> List.of("CONFIRMED", "CANCELLED");
                case "CONFIRMED" -> List.of("PROCESSING");
                case "PROCESSING" -> List.of("SHIPPED");
                case "SHIPPED" -> List.of("DELIVERED");
                case "DELIVERED" -> List.of("COMPLETED");
                case "COMPLETED", "CANCELLED" -> List.of();
                default -> List.of();
            };

            if (!allowedNextStatuses.contains(target)) {
                throw new IllegalStateException("Không thể chuyển từ " + current + " sang " + target);
            }

            order.setStatus(target);
            order.setUpdatedAt(LocalDateTime.now());
            return orderRepository.save(order);
        });
    }

    @Override
    public void deleteOrder(Integer id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order not found: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public boolean existsById(Integer id) {
        return orderRepository.existsById(id);
    }

    private String normalizeStatus(String status) {
        return status == null ? null : status.trim().toUpperCase();
    }

    private void autoCalculateTotals(Order order) {
        BigDecimal items = BigDecimal.ZERO;

        if (order.getOrderDetails() != null) {
            for (OrderDetail od : order.getOrderDetails()) {
                BigDecimal price = od.getUnitPrice() != null ? od.getUnitPrice() : BigDecimal.ZERO;
                int qty = od.getQuantity() != null ? od.getQuantity() : 0;
                items = items.add(price.multiply(BigDecimal.valueOf(qty)));
            }
        }

        BigDecimal discount = order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO;
        BigDecimal shipping = order.getShippingFee() != null ? order.getShippingFee() : BigDecimal.ZERO;

        BigDecimal total = items.subtract(discount).add(shipping);
        if (total.compareTo(BigDecimal.ZERO) < 0)
            total = BigDecimal.ZERO;

        order.setTotalAmount(total);
    }

    @Override
    @Transactional
    public Optional<Order> getOrderByIdWithDetails(Integer id) {
        return orderRepository.findOneWithDetailsById(id);
    }

    // OrderServiceImpl.java
    @Override
    public boolean cancelByUser(Integer id, String email) {
        return orderRepository.findById(id).map(o -> {
            if (!o.getUser().getEmail().equalsIgnoreCase(email))
                return false;
            String s = o.getStatus();
            if (List.of("Pending Confirmation", "CONFIRMED", "PROCESSING").contains(s)) {
                o.setStatus("CANCELLED");
                o.setUpdatedAt(LocalDateTime.now());
                orderRepository.save(o);
                return true;
            }
            return false;
        }).orElse(false);
    }

    @Override
    public boolean markReceivedByUser(Integer id, String email) {
        return orderRepository.findById(id).map(o -> {
            if (!o.getUser().getEmail().equalsIgnoreCase(email))
                return false;
            String s = o.getStatus();
            if (List.of("SHIPPED", "DELIVERED").contains(s)) {
                o.setStatus("COMPLETED");
                o.setUpdatedAt(LocalDateTime.now());
                orderRepository.save(o);
                return true;
            }
            return false;
        }).orElse(false);
    }

}
