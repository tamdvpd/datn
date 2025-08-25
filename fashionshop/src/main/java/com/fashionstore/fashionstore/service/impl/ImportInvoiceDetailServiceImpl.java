package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.ImportInvoice;
import com.fashionstore.fashionstore.entity.ImportInvoiceDetail;
import com.fashionstore.fashionstore.entity.ProductDetail;
import com.fashionstore.fashionstore.repository.ImportInvoiceDetailRepository;
import com.fashionstore.fashionstore.repository.ImportInvoiceRepository;
import com.fashionstore.fashionstore.repository.ProductDetailRepository;
import com.fashionstore.fashionstore.service.ImportInvoiceDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportInvoiceDetailServiceImpl implements ImportInvoiceDetailService {

    private final ImportInvoiceDetailRepository importInvoiceDetailRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ImportInvoiceRepository importInvoiceRepository;

    @Override
    public List<ImportInvoiceDetail> getAll() {
        return importInvoiceDetailRepository.findAll();
    }

    @Override
    public List<ImportInvoiceDetail> getByImportInvoiceId(Integer importInvoiceId) {
        return importInvoiceDetailRepository.findByImportInvoiceId(importInvoiceId);
    }

    @Override
    public ImportInvoiceDetail getById(Integer id) {
        return importInvoiceDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy chi tiết phiếu nhập với id " + id));
    }

    @Override
    @Transactional
    public ImportInvoiceDetail create(ImportInvoiceDetail detail) {
        // Lấy ProductDetail từ DB
        ProductDetail pd = productDetailRepository.findById(detail.getProductDetail().getId())
                .orElseThrow(() -> new RuntimeException("❌ Chi tiết sản phẩm không tồn tại"));

        // Cộng thêm tồn kho
        pd.setQuantity(pd.getQuantity() + detail.getQuantity());
        productDetailRepository.save(pd);
        detail.setProductDetail(pd);

        // Lấy ImportInvoice từ DB
        ImportInvoice invoice = importInvoiceRepository.findById(detail.getImportInvoice().getId())
                .orElseThrow(() -> new RuntimeException("❌ Phiếu nhập không tồn tại"));
        detail.setImportInvoice(invoice);

        return importInvoiceDetailRepository.save(detail);
    }

    @Override
    @Transactional
    public ImportInvoiceDetail update(Integer id, ImportInvoiceDetail detail) {
        ImportInvoiceDetail existing = importInvoiceDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Chi tiết phiếu nhập không tồn tại"));

        // Lấy ProductDetail từ DB
        ProductDetail pd = productDetailRepository.findById(detail.getProductDetail().getId())
                .orElseThrow(() -> new RuntimeException("❌ Chi tiết sản phẩm không tồn tại"));

        // Tính chênh lệch số lượng và cập nhật tồn kho
        int diffQuantity = detail.getQuantity() - existing.getQuantity();
        pd.setQuantity(pd.getQuantity() + diffQuantity);
        productDetailRepository.save(pd);

        // Cập nhật thông tin chi tiết
        existing.setQuantity(detail.getQuantity());
        existing.setUnitPrice(detail.getUnitPrice());
        existing.setProductDetail(pd);
        existing.setUser(detail.getUser());

        // Cập nhật ImportInvoice nếu có
        if (detail.getImportInvoice() != null) {
            ImportInvoice invoice = importInvoiceRepository.findById(detail.getImportInvoice().getId())
                    .orElseThrow(() -> new RuntimeException("❌ Phiếu nhập không tồn tại"));
            existing.setImportInvoice(invoice);
        }

        return importInvoiceDetailRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        ImportInvoiceDetail existing = importInvoiceDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Chi tiết phiếu nhập không tồn tại"));

        ProductDetail pd = existing.getProductDetail();

        //  Chỉ cho phép xóa nếu kho đã hết hàng
        if (pd.getQuantity() > 0) {
            throw new RuntimeException("⚠️ Sản phẩm còn tồn kho, không thể xóa chi tiết nhập này!");
        }

        importInvoiceDetailRepository.deleteById(id);
    }

    @Override
    public Double getTotalAmountByInvoice(Integer importInvoiceId) {
        List<ImportInvoiceDetail> details = importInvoiceDetailRepository.findByImportInvoiceId(importInvoiceId);

        return details.stream()
                .map(d -> d.getUnitPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doubleValue();
    }
}
