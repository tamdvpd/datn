package com.fashionstore.fashionstore.repository;

<<<<<<< Updated upstream
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fashionstore.fashionstore.entity.Order;
import com.fashionstore.fashionstore.entity.ShippingProvider;

public interface ShippingProviderRepository extends JpaRepository<ShippingProvider, Integer> {
    Optional<ShippingProvider> findByCode(String code);

    Optional<Order> findById(Long shippingProviderId);
=======
import org.springframework.data.jpa.repository.JpaRepository;

import com.fashionstore.fashionstore.entity.ShippingProvider;

public interface ShippingProviderRepository extends JpaRepository<ShippingProvider, Integer> {

>>>>>>> Stashed changes
}
