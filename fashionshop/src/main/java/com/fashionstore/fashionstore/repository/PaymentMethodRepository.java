package com.fashionstore.fashionstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fashionstore.fashionstore.entity.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
    List<PaymentMethod> findByStatusTrue();

}
