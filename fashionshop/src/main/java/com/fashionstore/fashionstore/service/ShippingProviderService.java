package com.fashionstore.fashionstore.service;

import java.util.List;
import java.util.Optional;

import com.fashionstore.fashionstore.entity.ShippingProvider;

public interface ShippingProviderService {
    List<ShippingProvider> getAllShipping();

    Optional<ShippingProvider> getById(Integer id);

    ShippingProvider create(ShippingProvider provider);

    ShippingProvider update(Integer id, ShippingProvider provider);

    void delete(Integer id);
}
