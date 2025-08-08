package com.fashionstore.fashionstore.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fashionstore.fashionstore.entity.PaymentMethod;
import com.fashionstore.fashionstore.repository.PaymentMethodRepository;
import com.fashionstore.fashionstore.service.PaymentMethodService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethodRepository.findAll();
    }

    @Override
    public List<PaymentMethod> getAllPaymentMethodsIsActive() {
        return paymentMethodRepository.findByStatusTrue();
    }

    @Override
    public Optional<PaymentMethod> getPaymentMethodById(Integer id) {
        return paymentMethodRepository.findById(id);
    }

    @Override
    public PaymentMethod updatePaymentMethod(Integer id, PaymentMethod paymentMethod) {
        paymentMethod.setId(id);
        return paymentMethodRepository.save(paymentMethod);
    }

}
