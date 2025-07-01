package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.Supplier;
import com.fashionstore.fashionstore.repository.SupplierRepository;
import com.fashionstore.fashionstore.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    public Optional<Supplier> getSupplierById(Integer id) {
        return supplierRepository.findById(id);
    }

    @Override
    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier updateSupplier(Integer id, Supplier supplier) {
        supplier.setId(id);
        return supplierRepository.save(supplier);
    }

    @Override
    public void deleteSupplier(Integer id) {
        supplierRepository.deleteById(id);
    }
}
