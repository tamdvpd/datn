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
        validateAndCheckDuplicate(supplier.getEmail(), supplier.getPhoneNumber(), null);
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier updateSupplier(Integer id, Supplier supplier) {
        Supplier existing = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nhà cung cấp không tồn tại."));
        validateAndCheckDuplicate(supplier.getEmail(), supplier.getPhoneNumber(), id);

        // Cập nhật thông tin
        existing.setName(supplier.getName());
        existing.setEmail(supplier.getEmail());
        existing.setPhoneNumber(supplier.getPhoneNumber());
        existing.setAddress(supplier.getAddress());
        existing.setStatus(supplier.getStatus());

        return supplierRepository.save(existing);
    }

    @Override
    public void deleteSupplier(Integer id) {
        supplierRepository.deleteById(id);
    }

    /**
     * Kiểm tra định dạng và trùng lặp email/phone
     * 
     * @param email       Email cần kiểm tra
     * @param phoneNumber Số điện thoại cần kiểm tra
     * @param currentId   Id nhà cung cấp hiện tại (null nếu tạo mới)
     */
    private void validateAndCheckDuplicate(String email, String phoneNumber, Integer currentId) {
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

        // Kiểm tra email trùng với nhà cung cấp khác
        List<Supplier> emailMatches = supplierRepository.findByEmail(email);
        for (Supplier s : emailMatches) {
            if (currentId == null || !s.getId().equals(currentId)) {
                errors.add("Email đã tồn tại ở nhà cung cấp khác.");
                break;
            }
        }

        // Kiểm tra số điện thoại trùng với nhà cung cấp khác
        List<Supplier> phoneMatches = supplierRepository.findByPhoneNumber(phoneNumber);
        for (Supplier s : phoneMatches) {
            if (currentId == null || !s.getId().equals(currentId)) {
                errors.add("Số điện thoại đã tồn tại ở nhà cung cấp khác.");
                break;
            }
        }

        // Nếu có lỗi, ném ra RuntimeException
        if (!errors.isEmpty()) {
            throw new RuntimeException(String.join(" | ", errors));
        }
    }
}
