package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.dto.CreateOrderRequest;
import com.fashionstore.fashionstore.entity.Coupon;
import com.fashionstore.fashionstore.entity.Order;
import com.fashionstore.fashionstore.entity.OrderDetail;
import com.fashionstore.fashionstore.entity.PaymentMethod;
import com.fashionstore.fashionstore.entity.ProductDetail;
import com.fashionstore.fashionstore.entity.ShippingProvider;
import com.fashionstore.fashionstore.entity.User;
import com.fashionstore.fashionstore.repository.CouponRepository;
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
    private final OrderRepository orderRepo;
    private final OrderDetailRepository orderDetailRepo;
    private final ProductDetailRepository productDetailRepo;
    private final UserRepository userRepo;
    private final CouponRepository couponRepo;
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
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepo.findByUserId(userId);
    }

    public List<Order> getOrdersByUserAndStatus(Long userId, String status) {
        return orderRepo.findByUserIdAndStatus(userId, status);
    }
     @Transactional
    public Order createOrder(CreateOrderRequest request) {
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;

        User user = userRepo.findById(request.getUserId()).orElseThrow();
        Coupon coupon = null;
        if (request.getCouponCode() != null) {
            coupon = couponRepo.findByCode(request.getCouponCode()).orElse(null);
            if (coupon != null && coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Coupon expired");
            }
        }

        Order order = new Order();
        order.setUser(user);
        order.setShippingProvider(new ShippingProvider(request.getShippingProviderId()));
        order.setPaymentMethod(new PaymentMethod(request.getPaymentMethodId()));
        order.setCoupon(coupon);
        order.setReceiverName(request.getReceiverName());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setReceiverAddress(request.getReceiverAddress());
        order.setNote(request.getNote());
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("PENDING");

        order = orderRepo.save(order);

        for (CreateOrderRequest.ProductOrderItem item : request.getProducts()) {
            ProductDetail detail = productDetailRepo.findById(item.getProductDetailId()).orElseThrow();
            if (detail.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Out of stock");
            }

            BigDecimal itemTotal = detail.getDiscountPrice() != null
                    ? detail.getDiscountPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                    : detail.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);

            detail.setQuantity(detail.getQuantity() - item.getQuantity());
            productDetailRepo.save(detail);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProductDetail(detail);
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setUnitPrice(detail.getDiscountPrice() != null ? detail.getDiscountPrice() : detail.getPrice());
            orderDetailRepo.save(orderDetail);
        }

        if (coupon != null) {
            discount = total.multiply(BigDecimal.valueOf(coupon.getDiscountPercent())).divide(BigDecimal.valueOf(100));
        }

        order.setTotalAmount(total);
        order.setDiscountAmount(discount);
        order.setShippingFee(BigDecimal.valueOf(30000)); //phí ship cố định
        order.setUpdatedAt(LocalDateTime.now());
        orderRepo.save(order);

        return order;
    }

}
    