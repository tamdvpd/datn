package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    List<Payment> getAllPayments();
    Page<Payment> getAllPayments(Pageable pageable);
    Optional<Payment> getPaymentById(Integer id);
    Payment createPayment(Payment payment);
    Payment updatePayment(Integer id, Payment payment);
    void deletePayment(Integer id);
}