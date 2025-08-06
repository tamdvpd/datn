package com.fashionstore.fashionstore.service;

import java.util.Map;

public interface CheckOutService {
    Map<String, Object> reviewOrder(Map<String, Object> payload);

    Map<String, Object> processPayment(Map<String, Object> payload);

    Map<String, Object> processPaymentCart(Map<String, Object> payload);

    void updateStatus(Integer orderId, String status);
}
