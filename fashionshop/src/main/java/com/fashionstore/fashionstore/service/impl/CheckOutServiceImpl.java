package com.fashionstore.fashionstore.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.fashionstore.fashionstore.entity.Coupon;
import com.fashionstore.fashionstore.entity.Order;
import com.fashionstore.fashionstore.entity.OrderDetail;
import com.fashionstore.fashionstore.entity.PaymentMethod;
import com.fashionstore.fashionstore.entity.ProductDetail;
import com.fashionstore.fashionstore.entity.ShippingProvider;
import com.fashionstore.fashionstore.entity.User;
import com.fashionstore.fashionstore.service.CartService;
import com.fashionstore.fashionstore.service.CheckOutService;
import com.fashionstore.fashionstore.service.CouponService;
import com.fashionstore.fashionstore.service.OrderDetailService;
import com.fashionstore.fashionstore.service.OrderService;
import com.fashionstore.fashionstore.service.PaymentMethodService;
import com.fashionstore.fashionstore.service.ProductDetailService;
import com.fashionstore.fashionstore.service.ShippingProviderService;
import com.fashionstore.fashionstore.service.VNPayService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckOutServiceImpl implements CheckOutService {
    private final ProductDetailService productDetailService;
    private final ShippingProviderService shippingProviderService;
    private final PaymentMethodService paymentMethodService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final CouponService couponService;
    private final VNPayService vnPayService;
    private final CartService cartService;

    @Override
    public Map<String, Object> reviewOrder(Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();

        try {
            Integer id = Integer.valueOf(payload.get("id").toString());
            Object quantityObj = payload.get("quantity");

            // Kiểm tra null
            if (quantityObj == null) {
                response.put("success", false);
                response.put("message", "Số lượng không được để trống!");
                return response;
            }

            // Kiểm tra kiểu và ép kiểu
            int quantity;
            try {
                double quantityDouble = Double.parseDouble(quantityObj.toString());

                // Kiểm tra xem có phải là số nguyên không
                if (quantityDouble % 1 != 0) {
                    response.put("success", false);
                    response.put("message", "Số lượng phải là số nguyên!");
                    return response;
                }

                quantity = (int) quantityDouble;

                if (quantity <= 0) {
                    response.put("success", false);
                    response.put("message", "Số lượng phải lớn hơn 0!");
                    return response;
                }

            } catch (NumberFormatException e) {
                response.put("success", false);
                response.put("message", "Số lượng không hợp lệ!");
                return response;
            }

            int checkQuantity = productDetailService.getProductDetailById(id).get().getQuantity();

            boolean result = quantity <= checkQuantity;

            response.put("success", result);
            response.put("message", result
                    ? "OK"
                    : "Sản phẩm tồn kho là: " + checkQuantity + ", không đủ với số lượng mà bạn muốn mua!");
            return response;

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Đã có lỗi xảy ra: " + e.getMessage());
            return response;
        }
    }

    @Override
    public Map<String, Object> processPayment(Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();

        // đọc từ frontend
        String fullName = payload.get("receiverName").toString();
        String phoneNumber = payload.get("receiverPhone").toString();
        String address = payload.get("receiverAddress").toString();
        int couponId = Integer.parseInt(payload.get("couponId").toString());
        int quantity = Integer.parseInt(payload.get("quantity").toString());
        int productDetailId = Integer.parseInt(payload.get("productDetailId").toString());

        Order order = new Order();
        OrderDetail orderDetail = new OrderDetail();
        User user = new User();

        PaymentMethod paymentMethod = paymentMethodService
                .getPaymentMethodById(Integer.valueOf(payload.get("paymentMethodId").toString())).get();
        user.setId(Integer.valueOf(payload.get("userId").toString()));
        ShippingProvider shippingProvider = new ShippingProvider();
        shippingProvider.setId(Integer.valueOf(payload.get("shippingProviderId").toString()));
        shippingProvider = shippingProviderService
                .getById(Integer.valueOf(payload.get("shippingProviderId").toString())).get();
        Coupon coupon = new Coupon();
        Optional<Coupon> couponOpt = couponService.getCouponById(couponId);
        if (couponOpt.isPresent()) {
            coupon = couponOpt.get();
        } else {
            coupon.setId(0);
        }
        ProductDetail productDetail = productDetailService
                .getProductDetailById(productDetailId).get();
        // Tổng hóa đơn
        BigDecimal totalAmount = null;
        // Tổng giảm giá
        BigDecimal discountAmount = null;
        if (couponId != 0) {
            // tính tổng tiền có mã giảm giá
            if (productDetail.getDiscountPrice() != null
                    && productDetail.getDiscountPrice().compareTo(BigDecimal.ZERO) > 0) {
                // tính tổng tiền khi mà sản phẩm chi tiết có giảm giá
                // lấy phần trăm giảm giá của mã giảm giá
                order.setCoupon(coupon);
                double discountPercent = coupon.getDiscountPercent() / 100.0;
                // lấy giá tiền sau khi giảm của option sản phẩm
                // Tính tổng giá trị
                BigDecimal total = productDetail.getDiscountPrice().multiply(BigDecimal.valueOf(quantity));
                // Tính tổng số tiền giảm
                discountAmount = total.multiply(BigDecimal.valueOf(discountPercent));
                order.setDiscountAmount(discountAmount);
                // tính tổng hóa đơn -giảm giá, +phí ship
                totalAmount = total.subtract(discountAmount).add(shippingProvider.getShippingFee());
                order.setTotalAmount(totalAmount);
                // set đơn giá hóa đơn chi tiêt
                orderDetail.setUnitPrice(productDetail.getDiscountPrice());
            } else {
                // tính tổng tiền khi mà sản phẩm chi tiết chỉ có giá gốc
                // lấy phần trăm giảm giá của mã giảm giá
                order.setCoupon(coupon);
                double discountPercent = coupon.getDiscountPercent() / 100.0;
                // lấy giá gốc của option sản phẩm
                // Tính tổng giá trị
                BigDecimal total = productDetail.getPrice().multiply(BigDecimal.valueOf(quantity));
                // Tính tổng số tiền giảm
                discountAmount = total.multiply(BigDecimal.valueOf(discountPercent));
                order.setDiscountAmount(discountAmount);
                // tính tổng hóa đơn -giảm giá, +phí ship
                totalAmount = total.subtract(discountAmount).add(shippingProvider.getShippingFee());
                order.setTotalAmount(totalAmount);
                // set đơn giá hóa đơn chi tiêt
                orderDetail.setUnitPrice(productDetail.getPrice());
            }
        } else {
            // tính tổng tiền không có mã giảm giá
            if (productDetail.getDiscountPrice() != null
                    && productDetail.getDiscountPrice().compareTo(BigDecimal.ZERO) > 0) {
                // tính tổng tiền khi mà sản phẩm chi tiết có giảm giá
                // lấy giá tiền sau khi giảm của option sản phẩm
                // Tính tổng giá trị
                BigDecimal total = productDetail.getDiscountPrice().multiply(BigDecimal.valueOf(quantity));
                // cộng phí ship
                totalAmount = total.add(shippingProvider.getShippingFee());
                order.setDiscountAmount(BigDecimal.ZERO);
                order.setTotalAmount(totalAmount);
                // set đơn giá hóa đơn chi tiêt
                orderDetail.setUnitPrice(productDetail.getDiscountPrice());
            } else {
                // tính tổng tiền khi mà sản phẩm chi tiết chỉ có giá gốc
                // lấy giá gốc của option sản phẩm
                // Tính tổng giá trị
                BigDecimal total = productDetail.getPrice().multiply(BigDecimal.valueOf(quantity));
                // Tính tổng số + phí ship
                totalAmount = total.add(shippingProvider.getShippingFee());
                order.setDiscountAmount(BigDecimal.ZERO);
                order.setTotalAmount(totalAmount);
                // set đơn giá hóa đơn chi tiêt
                orderDetail.setUnitPrice(productDetail.getPrice());
            }
        }
        // đọc productDetailId để tính tổng tiền
        // tạo order
        order.setUser(user);
        order.setShippingProvider(shippingProvider);
        order.setPaymentMethod(paymentMethod);
        order.setShippingFee(shippingProvider.getShippingFee());
        order.setNote(payload.get("note").toString());
        order.setReceiverName(fullName);
        order.setReceiverPhone(phoneNumber);
        order.setReceiverAddress(address);
        if (paymentMethod.getCode().equalsIgnoreCase("COD")) {
            // set status là COD thanh toán khi nhận hàng
            order.setStatus("Pending Confirmation");
            orderService.createOrder(order);
            response.put("status", "success");
            response.put("orderId", order.getId());
            response.put("totalAmount", order.getTotalAmount());
            orderDetail.setOrder(order);
            orderDetail.setProductDetail(productDetail);
            orderDetail.setQuantity(quantity);
            orderDetailService.createOrderDetail(orderDetail);
            // cập nhật số lượng option của sản phẩm
            productDetailService.updateQuantity(productDetailId, quantity);
            // cập nhật lại số lượng (-1) mã giá nếu có dùng
            if (couponId != 0) {
                couponService.updateQuantity(couponId);
            }
        } else if (paymentMethod.getCode().equalsIgnoreCase("VNPAY")) {
            // set status là PENDING_PAYMENT chờ thanh toán rồi lưu vào database
            order.setStatus("Pending Payment");
            orderService.createOrder(order);
            orderDetail.setOrder(order);
            orderDetail.setProductDetail(productDetail);
            orderDetail.setQuantity(quantity);
            orderDetailService.createOrderDetail(orderDetail);
            // cập nhật số lượng option của sản phẩm
            productDetailService.updateQuantity(productDetailId, quantity);
            // cập nhật lại số lượng (-1) mã giá nếu có dùng
            if (couponId != 0) {
                couponService.updateQuantity(couponId);
            }
            response.put("orderId", order.getId());
            String paymentUrl = useVNPay(order.getTotalAmount().intValue(), order.getId().toString(),
                    "http://localhost:8080");
            response.put("paymentUrl", paymentUrl);
        }
        return response;
    }

    public String useVNPay(int amount, String orderInfo, String baseUrl) {
        return vnPayService.createOrder(amount, orderInfo, baseUrl);
    }

    @Override
    public void updateStatus(Integer orderId, String status) {
        Order order = orderService.getOrderById(orderId).get();
        if (order != null) {
            order.setStatus(status);
            orderService.createOrder(order);
        }
    }

    @Override
    public Map<String, Object> processPaymentCart(Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();

        // Đọc thông tin chung từ payload
        String fullName = payload.get("receiverName").toString();
        String phoneNumber = payload.get("receiverPhone").toString();
        String address = payload.get("receiverAddress").toString();
        int couponId = Integer.parseInt(payload.get("couponId").toString());

        // Tạo đơn hàng
        Order order = new Order();
        User user = new User();

        PaymentMethod paymentMethod = paymentMethodService
                .getPaymentMethodById(Integer.valueOf(payload.get("paymentMethodId").toString())).get();
        user.setId(Integer.valueOf(payload.get("userId").toString()));
        ShippingProvider shippingProvider = new ShippingProvider();
        shippingProvider.setId(Integer.valueOf(payload.get("shippingProviderId").toString()));
        shippingProvider = shippingProviderService
                .getById(Integer.valueOf(payload.get("shippingProviderId").toString())).get();
        Coupon coupon = new Coupon();
        Optional<Coupon> couponOpt = couponService.getCouponById(couponId);
        if (couponOpt.isPresent()) {
            coupon = couponOpt.get();
        } else {
            coupon.setId(0);
        }
        // Tổng hóa đơn
        BigDecimal totalAmount = BigDecimal.ZERO;
        // Tổng giảm giá
        BigDecimal discountAmount = BigDecimal.ZERO;
        List<Map<String, Object>> orderItems = (List<Map<String, Object>>) payload.get("orderItems");
        // Tính tổng hóa đơn
        for (Map<String, Object> item : orderItems) {
            int productDetailId = Integer.parseInt(item.get("productDetailId").toString());
            System.out.println("id của option sản phẩm: " + productDetailId);
            int quantity = Integer.parseInt(item.get("quantity").toString());
            System.out.println("số lượng của option định mua: " + quantity);
            ProductDetail productDetail = productDetailService.getProductDetailById(productDetailId).get();
            if (productDetail.getDiscountPrice() != null
                    && productDetail.getDiscountPrice().compareTo(BigDecimal.ZERO) > 0) {
                totalAmount = totalAmount.add(productDetail.getDiscountPrice().multiply(BigDecimal.valueOf(quantity)));
            } else {
                totalAmount = totalAmount.add(productDetail.getPrice().multiply(BigDecimal.valueOf(quantity)));
            }
        }
        // Giảm giá nếu có
        if (coupon.getId() != 0) {
            // số tiền giảm
            discountAmount = totalAmount.multiply(BigDecimal.valueOf(coupon.getDiscountPercent() / 100.0));
            totalAmount = totalAmount.subtract(discountAmount);
            order.setCoupon(coupon);
        }
        totalAmount = totalAmount.add(shippingProvider.getShippingFee());
        order.setUser(user);
        order.setShippingProvider(shippingProvider);
        order.setPaymentMethod(paymentMethod);
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(discountAmount);
        order.setShippingFee(shippingProvider.getShippingFee());
        order.setNote(payload.get("note").toString());
        order.setReceiverName(fullName);
        order.setReceiverPhone(phoneNumber);
        order.setReceiverAddress(address);
        if (paymentMethod.getCode().equalsIgnoreCase("COD")) {
            // set status là COD thanh toán khi nhận hàng
            order.setStatus("Pending Confirmation");
            orderService.createOrder(order);
            cartService.clearCart(user.getId());
            response.put("status", "success");
            response.put("orderId", order.getId());
            response.put("totalAmount", order.getTotalAmount());
            // dùng vòng lặp để thêm vào orderDetail
            for (Map<String, Object> item : orderItems) {
                OrderDetail orderDetail = new OrderDetail();
                int productDetailId = Integer.parseInt(item.get("productDetailId").toString());
                int quantity = Integer.parseInt(item.get("quantity").toString());
                ProductDetail productDetail = productDetailService.getProductDetailById(productDetailId).get();
                if (productDetail.getDiscountPrice() != null
                        && productDetail.getDiscountPrice().compareTo(BigDecimal.ZERO) > 0) {
                    // option có giảm giá
                    orderDetail.setOrder(order);
                    orderDetail.setProductDetail(productDetail);
                    orderDetail.setQuantity(quantity);
                    orderDetail.setUnitPrice(productDetail.getDiscountPrice());
                    orderDetailService.createOrderDetail(orderDetail);
                    // cập nhật số lượng option của sản phẩm
                    productDetailService.updateQuantity(productDetailId, quantity);
                    // cập nhật lại số lượng (-1) mã giá nếu có dùng
                    if (couponId != 0) {
                        couponService.updateQuantity(couponId);
                    }
                } else {
                    // option chỉ có giá gốc
                    orderDetail.setOrder(order);
                    orderDetail.setProductDetail(productDetail);
                    orderDetail.setQuantity(quantity);
                    orderDetail.setUnitPrice(productDetail.getPrice());
                    orderDetailService.createOrderDetail(orderDetail);
                    // cập nhật số lượng option của sản phẩm
                    productDetailService.updateQuantity(productDetailId, quantity);
                    // cập nhật lại số lượng (-1) mã giá nếu có dùng
                    if (couponId != 0) {
                        couponService.updateQuantity(couponId);
                    }
                }
            }
        } else if (paymentMethod.getCode().equalsIgnoreCase("VNPAY")) {
            // set status là COD thanh toán khi nhận hàng
            order.setStatus("Pending Payment");
            orderService.createOrder(order);
            cartService.clearCart(user.getId());
            // dùng vòng lặp để thêm vào orderDetail
            for (Map<String, Object> item : orderItems) {
                OrderDetail orderDetail = new OrderDetail();
                int productDetailId = Integer.parseInt(item.get("productDetailId").toString());
                int quantity = Integer.parseInt(item.get("quantity").toString());
                ProductDetail productDetail = productDetailService.getProductDetailById(productDetailId).get();
                if (productDetail.getDiscountPrice() != null
                        && productDetail.getDiscountPrice().compareTo(BigDecimal.ZERO) > 0) {
                    // option có giảm giá
                    orderDetail.setOrder(order);
                    orderDetail.setProductDetail(productDetail);
                    orderDetail.setQuantity(quantity);
                    orderDetail.setUnitPrice(productDetail.getDiscountPrice());
                    orderDetailService.createOrderDetail(orderDetail);
                    // cập nhật số lượng option của sản phẩm
                    productDetailService.updateQuantity(productDetailId, quantity);
                    // cập nhật lại số lượng (-1) mã giá nếu có dùng
                    if (couponId != 0) {
                        couponService.updateQuantity(couponId);
                    }
                } else {
                    // option chỉ có giá gốc
                    orderDetail.setOrder(order);
                    orderDetail.setProductDetail(productDetail);
                    orderDetail.setQuantity(quantity);
                    orderDetail.setUnitPrice(productDetail.getPrice());
                    orderDetailService.createOrderDetail(orderDetail);
                    // cập nhật số lượng option của sản phẩm
                    productDetailService.updateQuantity(productDetailId, quantity);
                    // cập nhật lại số lượng (-1) mã giá nếu có dùng
                    if (couponId != 0) {
                        couponService.updateQuantity(couponId);
                    }
                }
            }
            response.put("orderId", order.getId());
            String paymentUrl = useVNPay(order.getTotalAmount().intValue(), order.getId().toString(),
                    "http://localhost:8080");
            response.put("paymentUrl", paymentUrl);
        }
        return response;
    }
}
