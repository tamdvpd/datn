package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.dto.CreateOrderRequest;
import com.fashionstore.fashionstore.entity.*;
import com.fashionstore.fashionstore.repository.*;
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

    private final OrderRepository orderRepo;
    private final OrderDetailRepository orderDetailRepo;
    private final ProductDetailRepository productDetailRepo;
    private final UserRepository userRepo;
    private final CouponRepository couponRepo;
    private final InventoryLogRepository inventoryLogRepo;
    private final ShippingProviderRepository shippingProviderRepo;
    private final PaymentMethodRepository paymentMethodRepo;

    @Override
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepo.findAll(pageable);
    }

    @Override
    public Optional<Order> getOrderById(Integer id) {
        return orderRepo.findById(id);
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepo.save(order);
    }

    @Override
    public Order updateOrder(Integer id, Order order) {
        order.setId(id);
        return orderRepo.save(order);
    }

    @Override
    public void deleteOrder(Integer id) {
        orderRepo.deleteById(id);
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

        User user = userRepo.findById(request.getUserId().intValue())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        ShippingProvider shippingProvider = shippingProviderRepo.findById(request.getShippingProviderId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn vị vận chuyển"));

        PaymentMethod paymentMethod = paymentMethodRepo.findById(request.getPaymentMethodId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phương thức thanh toán"));

        Coupon coupon = null;
        if (request.getCouponCode() != null) {
            coupon = couponRepo.findByCode(request.getCouponCode()).orElse(null);
            if (coupon != null && coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Mã giảm giá đã hết hạn");
            }
        }

        // Tạo đơn hàng
        Order order = new Order();
        order.setUser(user);
        order.setShippingProvider(shippingProvider);
        order.setPaymentMethod(paymentMethod);
        order.setCoupon(coupon);
        order.setReceiverName(request.getReceiverName());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setReceiverAddress(request.getReceiverAddress());
        order.setNote(request.getNote());
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("PENDING");
        order = orderRepo.save(order);

        // Xử lý từng sản phẩm
        for (CreateOrderRequest.ProductOrderItem item : request.getProducts()) {
            ProductDetail detail = productDetailRepo.findById(item.getProductDetailId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết"));

            if (detail.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + detail.getId() + " không đủ hàng trong kho");
            }

            BigDecimal itemTotal = detail.getDiscountPrice() != null
                    ? detail.getDiscountPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                    : detail.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

            total = total.add(itemTotal);

            // Trừ kho
            detail.setQuantity(detail.getQuantity() - item.getQuantity());
            productDetailRepo.save(detail);

            // Ghi log tồn kho
            InventoryLog log = new InventoryLog();
            log.setProductDetail(detail);
            log.setAction("EXPORT");
            log.setQuantity(-item.getQuantity());
            log.setReferenceType("Order");
            log.setReferenceId(order.getId());
            log.setCreatedAt(LocalDateTime.now());
            inventoryLogRepo.save(log);

            // Chi tiết đơn hàng
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProductDetail(detail);
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setUnitPrice(detail.getDiscountPrice() != null ? detail.getDiscountPrice() : detail.getPrice());
            orderDetailRepo.save(orderDetail);
        }

        // Áp dụng giảm giá
        if (coupon != null) {
            discount = total.multiply(BigDecimal.valueOf(coupon.getDiscountPercent()))
                    .divide(BigDecimal.valueOf(100));
        }

        order.setTotalAmount(total);
        order.setDiscountAmount(discount);
        order.setShippingFee(BigDecimal.valueOf(30000)); // Phí ship cố định
        order.setUpdatedAt(LocalDateTime.now());
        order = orderRepo.save(order);

        return order;
    }
}
