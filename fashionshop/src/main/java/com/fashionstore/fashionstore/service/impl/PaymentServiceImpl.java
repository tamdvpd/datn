package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.Payment;
import com.fashionstore.fashionstore.repository.PaymentRepository;
import com.fashionstore.fashionstore.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Page<Payment> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }

    @Override
    public Optional<Payment> getPaymentById(Integer id) {
        return paymentRepository.findById(id);
    }

    @Override
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Payment updatePayment(Integer id, Payment payment) {
        payment.setId(id);
        return paymentRepository.save(payment);
    }

    @Override
    public void deletePayment(Integer id) {
        paymentRepository.deleteById(id);
    }
}