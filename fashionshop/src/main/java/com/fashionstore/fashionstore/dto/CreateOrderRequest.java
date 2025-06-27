package com.fashionstore.fashionstore.dto;

import java.util.List;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private Long userId;
    private Long shippingProviderId;
    private Long paymentMethodId;
    private String couponCode;
    private String note;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private List<ProductOrderItem> products;

    @Data
    public static class ProductOrderItem {
        private Long productDetailId;
        private int quantity;
    }
}

