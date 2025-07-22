package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.ImportInvoice;
import com.fashionstore.fashionstore.entity.Supplier;
import com.fashionstore.fashionstore.repository.ImportInvoiceRepository;
import com.fashionstore.fashionstore.repository.SupplierRepository;
import com.fashionstore.fashionstore.service.ImportInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImportInvoiceServiceImpl implements ImportInvoiceService {

    private final ImportInvoiceRepository importInvoiceRepository;
    private final SupplierRepository supplierRepository;

    @Override
    public List<ImportInvoice> getAllImportInvoices() {
        return importInvoiceRepository.findAll();
    }

    @Override
    public Optional<ImportInvoice> getImportInvoiceById(Integer id) {
        return importInvoiceRepository.findById(id);
    }

    @Override
    public ImportInvoice createImportInvoice(ImportInvoice invoice) {
        Integer supplierId = invoice.getSupplier().getId();

        // Kiểm tra nhà cung cấp tồn tại
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy nhà cung cấp"));

        // Kiểm tra trạng thái hoạt động
        if (!Boolean.TRUE.equals(supplier.getStatus())) {
            throw new RuntimeException("❌ Nhà cung cấp đã ngừng hoạt động. Không thể tạo phiếu nhập.");
        }

        // Gắn lại supplier từ DB để đảm bảo entity hợp lệ
        invoice.setSupplier(supplier);
        invoice.setImportDate(LocalDate.now()); // Cập nhật ngày nhập nếu cần
        return importInvoiceRepository.save(invoice);
    }

    @Override
    public ImportInvoice updateImportInvoice(Integer id, ImportInvoice invoice) {
        invoice.setId(id);
        return importInvoiceRepository.save(invoice);
    }

    @Override
    public void deleteImportInvoice(Integer id) {
        importInvoiceRepository.deleteById(id);
    }
}
