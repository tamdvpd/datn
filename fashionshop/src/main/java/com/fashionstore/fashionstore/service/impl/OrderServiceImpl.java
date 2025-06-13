package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.Order;
import com.fashionstore.fashionstore.entity.OrderDetail;
import com.fashionstore.fashionstore.entity.ProductDetail;
import com.fashionstore.fashionstore.entity.User;
import com.fashionstore.fashionstore.repository.OrderDetailRepository;
import com.fashionstore.fashionstore.repository.OrderRepository;
import com.fashionstore.fashionstore.repository.ProductDetailRepository;
import com.fashionstore.fashionstore.repository.UserRepository;
import com.fashionstore.fashionstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductDetailRepository productDetailRepository;
    private final UserRepository userRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Optional<Order> getOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Integer id, Order order) {
        order.setId(id);
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Order createOrderWithDetails(Order order) {
        if (order.getUser() != null && order.getUser().getId() != null) {
            User user = userRepository.findById(order.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            order.setUser(user);
        }

        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        BigDecimal totalAmount = BigDecimal.ZERO;

        Order savedOrder = orderRepository.save(order);

        for (OrderDetail detail : order.getOrderDetails()) {
            ProductDetail pd = productDetailRepository.findById(detail.getProductDetail().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + detail.getProductDetail().getId()));

            if (pd.getStockQuantity() < detail.getQuantity()) {
                throw new RuntimeException("Not enough stock for product ID: " + pd.getId());
            }

            pd.setStockQuantity(pd.getStockQuantity() - detail.getQuantity());
            productDetailRepository.save(pd);

            detail.setOrder(savedOrder);
            detail.setPrice(pd.getPrice());

            totalAmount = totalAmount.add(pd.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())));

            orderDetailRepository.save(detail);
        }

        savedOrder.setTotalAmount(totalAmount);
        savedOrder.setUpdatedAt(LocalDateTime.now());

        return orderRepository.save(savedOrder);
    }
}
