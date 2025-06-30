package com.fashionstore.fashionstore.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fashionstore.fashionstore.entity.ShippingProvider;
import com.fashionstore.fashionstore.repository.ShippingProviderRepository;
import com.fashionstore.fashionstore.service.ShippingProviderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShippingProviderServiceImpl implements ShippingProviderService {

    private final ShippingProviderRepository shippingProviderRepository;

    @Override
    public List<ShippingProvider> getAllShipping() {
        return shippingProviderRepository.findAll();
    }

    @Override
    public Optional<ShippingProvider> getById(Integer id) {
        return shippingProviderRepository.findById(id);
    }

    @Override
    public ShippingProvider create(ShippingProvider provider) {
        return shippingProviderRepository.save(provider);
    }

    @Override
    public ShippingProvider update(Integer id, ShippingProvider provider) {
        provider.setId(id);
        return shippingProviderRepository.save(provider);
    }

    @Override
    public void delete(Integer id) {
        shippingProviderRepository.deleteById(id);
    }
}
