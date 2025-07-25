package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.dto.order.CreateOrderRequest;
import com.fashionstore.fashionstore.dto.order.OrderResponse;
import com.fashionstore.fashionstore.entity.*;
import com.fashionstore.fashionstore.mapper.OrderMapper;
import com.fashionstore.fashionstore.repository.*;
import com.fashionstore.fashionstore.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ShippingProviderRepository shippingProviderRepository;
    private final CouponRepository couponRepository;

    private final OrderMapper orderMapper;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public OrderResponse getById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        Order order = new Order();
        order.setUser(
                userRepository.findById(request.getUserId().intValue())
                        .orElseThrow(() -> new RuntimeException("User không tồn tại")));
        order.setPaymentMethod(paymentMethodRepository.findById(request.getPaymentMethodId())
                .orElseThrow(() -> new RuntimeException("Phương thức thanh toán không hợp lệ")));
        order.setShippingProvider(shippingProviderRepository.findById(request.getShippingProviderId())
                .orElseThrow(() -> new RuntimeException("Đơn vị vận chuyển không hợp lệ")));

        if (request.getCouponId() != null) {
            couponRepository.findById(request.getCouponId()).ifPresent(order::setCoupon);
        }

        order.setReceiverName(request.getReceiverName());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setReceiverAddress(request.getReceiverAddress());
        order.setNote(request.getNote());
        order.setStatus(OrderStatus.PENDING);
        order.setDiscountAmount(java.math.BigDecimal.ZERO);
        order.setShippingFee(java.math.BigDecimal.ZERO);
        order.setTotalAmount(java.math.BigDecimal.ZERO);

        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }

    @Override
    @Transactional
    public OrderResponse update(Integer id, CreateOrderRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        order.setPaymentMethod(paymentMethodRepository.findById(request.getPaymentMethodId())
                .orElseThrow(() -> new RuntimeException("Phương thức thanh toán không hợp lệ")));
        order.setShippingProvider(shippingProviderRepository.findById(request.getShippingProviderId())
                .orElseThrow(() -> new RuntimeException("Đơn vị vận chuyển không hợp lệ")));

        if (request.getCouponId() != null) {
            couponRepository.findById(request.getCouponId()).ifPresent(order::setCoupon);
        } else {
            order.setCoupon(null);
        }

        order.setReceiverName(request.getReceiverName());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setReceiverAddress(request.getReceiverAddress());
        order.setNote(request.getNote());
        order.setUpdatedAt(LocalDateTime.now());

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public void delete(Integer id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Đơn hàng không tồn tại: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    public OrderResponse updateStatus(Integer id, OrderStatus status, Long changedBy, String note) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());

        // TODO: Ghi log lịch sử trạng thái nếu cần

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderResponse updateTrackingCode(Integer id, String trackingCode) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        order.setTrackingCode(trackingCode);
        order.setUpdatedAt(LocalDateTime.now());

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Page<OrderResponse> search(OrderStatus status, java.time.LocalDate from, java.time.LocalDate to,
            Pageable pageable) {
        java.time.LocalDateTime start = from != null ? from.atStartOfDay() : java.time.LocalDate.MIN.atStartOfDay();
        java.time.LocalDateTime end = to != null ? to.plusDays(1).atStartOfDay()
                : java.time.LocalDateTime.now().plusDays(1);
        return orderRepository.searchByStatusAndCreatedAtBetween(status, start, end, pageable)
                .map(orderMapper::toDto);
    }
}
