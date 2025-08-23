package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.Supplier;
import com.fashionstore.fashionstore.repository.SupplierRepository;
import com.fashionstore.fashionstore.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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
        checkDuplicateAndValidate(supplier.getEmail(), supplier.getPhoneNumber(), null);
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier updateSupplier(Integer id, Supplier supplier) {
        checkDuplicateAndValidate(supplier.getEmail(), supplier.getPhoneNumber(), id);
        supplier.setId(id);
        return supplierRepository.save(supplier);
    }

    @Override
    public void deleteSupplier(Integer id) {
        supplierRepository.deleteById(id);
    }

    private void checkDuplicateAndValidate(String email, String phoneNumber, Integer currentId) {
        List<String> errors = new ArrayList<>();

        // Kiểm tra định dạng số điện thoại
        if (!Pattern.matches("^0\\d{9}$", phoneNumber)) {
            errors.add("Số điện thoại không hợp lệ (phải bắt đầu bằng 0 và đủ 10 chữ số).");
        }

        // Kiểm tra định dạng email
        if (email != null && !email.isBlank()) {
            String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
            if (!Pattern.matches(emailRegex, email)) {
                errors.add("Email không đúng định dạng.");
            }
        }

        // Kiểm tra email đã tồn tại
        List<Supplier> emailMatches = supplierRepository.findByEmail(email);
        for (Supplier s : emailMatches) {
            if (currentId == null || !s.getId().equals(currentId)) {
                errors.add("Email đã tồn tại.");
                break;
            }
        }

        // Kiểm tra số điện thoại đã tồn tại
        List<Supplier> phoneMatches = supplierRepository.findByPhoneNumber(phoneNumber);
        for (Supplier s : phoneMatches) {
            if (currentId == null || !s.getId().equals(currentId)) {
                errors.add("Số điện thoại đã tồn tại.");
                break;
            }
        }

        // Nếu có lỗi thì ném ra RuntimeException chứa toàn bộ lỗi
        if (!errors.isEmpty()) {
            throw new RuntimeException(String.join(" | ", errors));
        }
    }
}