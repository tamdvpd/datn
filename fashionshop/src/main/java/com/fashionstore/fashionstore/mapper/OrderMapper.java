package com.fashionstore.fashionstore.mapper;

import com.fashionstore.fashionstore.dto.order.OrderItemResponse;
import com.fashionstore.fashionstore.dto.order.OrderResponse;
import com.fashionstore.fashionstore.entity.Order;
import com.fashionstore.fashionstore.entity.OrderDetail;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponse toDto(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .discountAmount(order.getDiscountAmount())
                .shippingFee(order.getShippingFee())
                .trackingCode(order.getTrackingCode())
                .note(order.getNote())
                .receiverName(order.getReceiverName())
                .receiverPhone(order.getReceiverPhone())
                .receiverAddress(order.getReceiverAddress())
                .paymentMethod(order.getPaymentMethod() != null ? order.getPaymentMethod().getCode() : null)
                .shippingProvider(order.getShippingProvider() != null ? order.getShippingProvider().getName() : null)
                .customerName(order.getUser() != null ? order.getUser().getFullName() : null)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(toItemDtoList(order.getOrderDetails()))
                .build();
    }

    private List<OrderItemResponse> toItemDtoList(List<OrderDetail> details) {
        if (details == null) return List.of();
        return details.stream()
                .map(detail -> OrderItemResponse.builder()
                        .productId(detail.getProductDetail().getProduct().getId())
                        .productName(detail.getProductDetail().getProduct().getName())
                        .color(detail.getProductDetail().getColor())
                        .size(detail.getProductDetail().getSize())
                        .imageUrl(detail.getProductDetail().getImageUrl())
                        .quantity(detail.getQuantity())
                        .unitPrice(detail.getUnitPrice())
                        .totalPrice(detail.getUnitPrice().multiply(
                                java.math.BigDecimal.valueOf(detail.getQuantity()))
                        )
                        .build())
                .collect(Collectors.toList());
    }
}
