package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.Supplier;

import java.util.List;
import java.util.Optional;

public interface SupplierService {

    // Lấy tất cả nhà cung cấp (kể cả ngừng hoạt động)
    List<Supplier> getAllSuppliers();

    // Lấy tất cả nhà cung cấp đang hoạt động (status = true)
    List<Supplier> getActiveSuppliers();

    // Lấy nhà cung cấp theo ID
    Optional<Supplier> getSupplierById(Integer id);

    // Tạo mới nhà cung cấp
    Supplier createSupplier(Supplier supplier);
    Supplier updateSupplier(Integer id, Supplier supplier);
    void deleteSupplier(Integer id);
}