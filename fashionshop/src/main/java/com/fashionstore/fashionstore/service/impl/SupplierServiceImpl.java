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
    public List<Supplier> getActiveSuppliers() {
        return supplierRepository.findByStatusTrue();
    }

    @Override
    public Optional<Supplier> getSupplierById(Integer id) {
        return supplierRepository.findById(id);
    }

    @Override
    public Supplier createSupplier(Supplier supplier) {
        checkDuplicate(supplier.getEmail(), supplier.getPhoneNumber(), null);
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier updateSupplier(Integer id, Supplier supplier) {
        checkDuplicate(supplier.getEmail(), supplier.getPhoneNumber(), id);
        supplier.setId(id);
        return supplierRepository.save(supplier);
    }

    @Override
    public void deleteSupplier(Integer id) {
        supplierRepository.deleteById(id);
    }

    // Kiểm tra trùng email hoặc số điện thoại (trừ bản ghi hiện tại nếu đang
    // update)
    private void checkDuplicate(String email, String phoneNumber, Integer currentId) {
        List<Supplier> emailMatches = supplierRepository.findByEmail(email);
        for (Supplier s : emailMatches) {
            if (currentId == null || !s.getId().equals(currentId)) {
                throw new RuntimeException("Email đã tồn tại!");
            }
        }

        List<Supplier> phoneMatches = supplierRepository.findByPhoneNumber(phoneNumber);
        for (Supplier s : phoneMatches) {
            if (currentId == null || !s.getId().equals(currentId)) {
                throw new RuntimeException("Số điện thoại đã tồn tại!");
            }
        }
    }
}
