package com.fashionstore.fashionstore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fashionstore.fashionstore.entity.ShippingProvider;

public interface ShippingProviderRepository extends JpaRepository<ShippingProvider, Integer> {
    Optional<ShippingProvider> findByCode(String code);

    List<ShippingProvider> findByStatusTrue();
}
