package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.ImportInvoice;
import com.fashionstore.fashionstore.entity.ImportInvoiceDetail;
import com.fashionstore.fashionstore.entity.ProductDetail;
import com.fashionstore.fashionstore.entity.Supplier;
import com.fashionstore.fashionstore.repository.ImportInvoiceDetailRepository;
import com.fashionstore.fashionstore.repository.ImportInvoiceRepository;
import com.fashionstore.fashionstore.repository.ProductDetailRepository;
import com.fashionstore.fashionstore.repository.SupplierRepository;
import com.fashionstore.fashionstore.service.ImportInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImportInvoiceServiceImpl implements ImportInvoiceService {

    private final ImportInvoiceRepository importInvoiceRepository;
    private final ImportInvoiceDetailRepository importInvoiceDetailRepository;
    private final SupplierRepository supplierRepository;
    private final ProductDetailRepository productDetailRepository;

    // ---------------- Phiếu nhập ----------------
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
        Supplier supplier = supplierRepository.findById(invoice.getSupplier().getId())
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy nhà cung cấp"));

        if (!Boolean.TRUE.equals(supplier.getStatus())) {
            throw new RuntimeException("❌ Nhà cung cấp đã ngừng hoạt động. Không thể tạo phiếu nhập.");
        }

        invoice.setSupplier(supplier);
        invoice.setImportDate(LocalDate.now());
        invoice.setStatus(ImportInvoice.Status.PENDING); // trạng thái mặc định
        return importInvoiceRepository.save(invoice);
    }

    @Override
    public ImportInvoice updateImportInvoice(Integer id, ImportInvoice invoice) {
        ImportInvoice existing = importInvoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Phiếu nhập không tồn tại"));

        if (existing.getStatus() == ImportInvoice.Status.COMPLETED) {
            throw new RuntimeException("❌ Không thể sửa phiếu nhập đã nhập kho");
        }

        existing.setSupplier(invoice.getSupplier());
        existing.setImportDate(invoice.getImportDate());
        existing.setNote(invoice.getNote());
        return importInvoiceRepository.save(existing);
    }

    @Override
    public void deleteImportInvoice(Integer id) {
        ImportInvoice existing = importInvoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Phiếu nhập không tồn tại"));

        if (existing.getStatus() == ImportInvoice.Status.COMPLETED) {
            throw new RuntimeException("❌ Không thể xóa phiếu nhập đã nhập kho");
        }

        importInvoiceRepository.delete(existing);
    }

    // ---------------- Chi tiết phiếu nhập ----------------
    @Override
    public ImportInvoiceDetail createInvoiceDetail(Integer invoiceId, ImportInvoiceDetail detail) {
        ImportInvoice invoice = importInvoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("❌ Phiếu nhập không tồn tại"));

        if (invoice.getStatus() == ImportInvoice.Status.COMPLETED) {
            throw new RuntimeException("❌ Không thể thêm chi tiết vào phiếu đã nhập kho");
        }

        ProductDetail pd = productDetailRepository.findById(detail.getProductDetail().getId())
                .orElseThrow(() -> new RuntimeException("❌ Chi tiết sản phẩm không tồn tại"));

        detail.setImportInvoice(invoice);
        detail.setProductDetail(pd);
        detail.setImported(false);
        return importInvoiceDetailRepository.save(detail);
    }

    @Override
    public ImportInvoiceDetail updateInvoiceDetail(Integer detailId, ImportInvoiceDetail detail) {
        ImportInvoiceDetail existingDetail = importInvoiceDetailRepository.findById(detailId)
                .orElseThrow(() -> new RuntimeException("❌ Chi tiết phiếu nhập không tồn tại"));

        if (existingDetail.getImportInvoice().getStatus() == ImportInvoice.Status.COMPLETED) {
            throw new RuntimeException("❌ Không thể sửa chi tiết phiếu đã nhập kho");
        }

        existingDetail.setQuantity(detail.getQuantity());
        existingDetail.setUnitPrice(detail.getUnitPrice());
        existingDetail.setProductDetail(detail.getProductDetail());
        existingDetail.setUser(detail.getUser());
        return importInvoiceDetailRepository.save(existingDetail);
    }

    @Override
    public void deleteInvoiceDetail(Integer detailId) {
        ImportInvoiceDetail existingDetail = importInvoiceDetailRepository.findById(detailId)
                .orElseThrow(() -> new RuntimeException("❌ Chi tiết phiếu nhập không tồn tại"));

        if (existingDetail.getImportInvoice().getStatus() == ImportInvoice.Status.COMPLETED) {
            throw new RuntimeException("❌ Không thể xóa chi tiết phiếu đã nhập kho");
        }

        importInvoiceDetailRepository.delete(existingDetail);
    }

    @Override
    public List<ImportInvoiceDetail> getInvoiceDetailsByInvoiceId(Integer invoiceId) {
        ImportInvoice invoice = importInvoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("❌ Phiếu nhập không tồn tại"));
        return invoice.getImportInvoiceDetails();
    }

    // ---------------- Nhập kho ----------------
    @Override
    @Transactional
    public void importStock(Integer invoiceId) {
        ImportInvoice invoice = importInvoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("❌ Phiếu nhập không tồn tại"));

        if (invoice.getStatus() == ImportInvoice.Status.COMPLETED) {
            throw new RuntimeException("❌ Phiếu nhập đã được nhập kho");
        }

        for (ImportInvoiceDetail detail : invoice.getImportInvoiceDetails()) {
            if (!detail.isImported()) {
                ProductDetail product = detail.getProductDetail();
                product.setQuantity(product.getQuantity() + detail.getQuantity());
                productDetailRepository.save(product);

                detail.setImported(true);
                importInvoiceDetailRepository.save(detail);
            }
        }

        invoice.setStatus(ImportInvoice.Status.COMPLETED);
        importInvoiceRepository.save(invoice);
    }
}

