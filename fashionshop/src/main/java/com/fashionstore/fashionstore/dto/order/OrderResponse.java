package com.fashionstore.fashionstore.dto.order;

import com.fashionstore.fashionstore.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {

    private Integer id;

    // Trạng thái đơn hàng
    private OrderStatus status;

    // Tổng tiền gốc
    private BigDecimal totalAmount;

    // Số tiền giảm
    private BigDecimal discountAmount;

    // Phí vận chuyển
    private BigDecimal shippingFee;

    // Mã vận đơn (GHN/GHTK...)
    private String trackingCode;

    // Ghi chú người mua
    private String note;

    // Người nhận
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;

    // Thời gian tạo - cập nhật
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Tên phương thức thanh toán
    private String paymentMethod;

    // Tên đơn vị vận chuyển
    private String shippingProvider;

    // Tên người đặt hàng
    private String customerName;

    // Danh sách sản phẩm trong đơn hàng
    private List<OrderItemResponse> items;
}
