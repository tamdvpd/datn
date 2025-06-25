package com.fashionstore.fashionstore.service;

import java.util.List;
import java.util.Optional;

import com.fashionstore.fashionstore.entity.PaymentMethod;

public interface PaymentMethodService {

    List<PaymentMethod> getAllPaymentMethods();

    Optional<PaymentMethod> getPaymentMethodById(Integer id);

    PaymentMethod updatePaymentMethod(Integer id, PaymentMethod paymentMethod);
}
