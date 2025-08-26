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
        ImportInvoice invoice = importInvoiceRepository.findById(detail.getImportInvoice().getId())
                .orElseThrow(() -> new RuntimeException("❌ Phiếu nhập không tồn tại"));

        if (invoice.getStatus() == ImportInvoice.Status.COMPLETED) {
            throw new RuntimeException("⚠️ Không thể thêm chi tiết cho phiếu đã nhập kho!");
        }

        ProductDetail pd = productDetailRepository.findById(detail.getProductDetail().getId())
                .orElseThrow(() -> new RuntimeException("❌ Chi tiết sản phẩm không tồn tại"));

        detail.setImportInvoice(invoice);
        detail.setProductDetail(pd);
        detail.setImported(false); // mặc định chưa nhập kho

        return importInvoiceDetailRepository.save(detail);
    }

    @Override
    @Transactional
    public ImportInvoiceDetail update(Integer id, ImportInvoiceDetail detail) {
        ImportInvoiceDetail existing = importInvoiceDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Chi tiết phiếu nhập không tồn tại"));

        if (existing.isImported() || existing.getImportInvoice().getStatus() == ImportInvoice.Status.COMPLETED) {
            throw new RuntimeException("⚠️ Không thể sửa chi tiết phiếu đã nhập kho!");
        }

        existing.setQuantity(detail.getQuantity());
        existing.setUnitPrice(detail.getUnitPrice());
        existing.setProductDetail(detail.getProductDetail());
        existing.setUser(detail.getUser());

        return importInvoiceDetailRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        ImportInvoiceDetail existing = importInvoiceDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Chi tiết phiếu nhập không tồn tại"));

        if (existing.isImported() || existing.getImportInvoice().getStatus() == ImportInvoice.Status.COMPLETED) {
            throw new RuntimeException("⚠️ Không thể xóa chi tiết phiếu đã nhập kho!");
        }

        importInvoiceDetailRepository.delete(existing);
    }

    @Override
    @Transactional
    public ImportInvoiceDetail importStock(Integer detailId) {
        ImportInvoiceDetail detail = importInvoiceDetailRepository.findById(detailId)
                .orElseThrow(() -> new RuntimeException("❌ Chi tiết phiếu nhập không tồn tại"));

        if (detail.isImported()) {
            throw new RuntimeException("⚠️ Sản phẩm đã nhập kho rồi!");
        }

        // Cộng tồn kho
        ProductDetail pd = detail.getProductDetail();
        pd.setQuantity(pd.getQuantity() + detail.getQuantity());
        productDetailRepository.save(pd);

        // Đánh dấu đã nhập
        detail.setImported(true);
        importInvoiceDetailRepository.save(detail);

        // Kiểm tra nếu tất cả chi tiết đã nhập → chuyển phiếu sang COMPLETED
        ImportInvoice invoice = detail.getImportInvoice();
        boolean allImported = invoice.getImportInvoiceDetails().stream().allMatch(ImportInvoiceDetail::isImported);
        if (allImported) {
            invoice.setStatus(ImportInvoice.Status.COMPLETED);
            importInvoiceRepository.save(invoice);
        }

        return detail;
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