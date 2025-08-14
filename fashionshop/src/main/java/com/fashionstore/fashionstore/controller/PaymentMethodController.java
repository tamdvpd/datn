package com.fashionstore.fashionstore.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fashionstore.fashionstore.entity.PaymentMethod;
import com.fashionstore.fashionstore.service.PaymentMethodService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3001/")
@RestController
@RequestMapping("/api/payment_method")
@RequiredArgsConstructor
public class PaymentMethodController {
    private final PaymentMethodService paymentMethodService;

    @GetMapping
    public ResponseEntity<List<PaymentMethod>> getAll() {
        return ResponseEntity.ok(paymentMethodService.getAllPaymentMethods());
    }

    @GetMapping("/active")
    public ResponseEntity<List<PaymentMethod>> getAllIsActive() {
        return ResponseEntity.ok(paymentMethodService.getAllPaymentMethodsIsActive());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethod> update(@PathVariable Integer id, @RequestBody PaymentMethod paymentMethod) {
        return ResponseEntity.ok(paymentMethodService.updatePaymentMethod(id, paymentMethod));
    }

}
