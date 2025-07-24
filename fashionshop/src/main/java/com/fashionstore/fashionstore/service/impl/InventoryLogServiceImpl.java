package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.InventoryLog;
import com.fashionstore.fashionstore.entity.ProductDetail;
import com.fashionstore.fashionstore.repository.InventoryLogRepository;
import com.fashionstore.fashionstore.repository.ProductDetailRepository;
import com.fashionstore.fashionstore.service.InventoryLogService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryLogServiceImpl implements InventoryLogService {

    private final InventoryLogRepository inventoryLogRepository;
    private final ProductDetailRepository productDetailRepository;

    @Override
    public List<InventoryLog> getAllInventoryLogs() {
        return inventoryLogRepository.findAll();
    }

    @Override
    public Optional<InventoryLog> getInventoryLogById(Integer id) {
        return inventoryLogRepository.findById(id);
    }

    @Override
    public InventoryLog createInventoryLog(InventoryLog log) {
        if (log.getProductDetail() == null || log.getProductDetail().getId() == null) {
            throw new IllegalArgumentException("ProductDetail is required.");
        }

        ProductDetail productDetail = productDetailRepository.findById(log.getProductDetail().getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy ProductDetail"));

        log.setProductDetail(productDetail);
        log.setCreatedAt(LocalDateTime.now());

        return inventoryLogRepository.save(log);
    }

    @Override
    public int getCurrentStock(Integer productDetailId) {
        Integer total = inventoryLogRepository.sumQuantityByProductDetailId(productDetailId);
        return total != null ? total : 0;
    }

    @Override
    @Transactional
    public void exportProduct(Integer productDetailId, int quantity, Integer orderId) {
        if (quantity <= 0) throw new IllegalArgumentException("Số lượng xuất phải > 0");

        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm"));

        int currentStock = productDetail.getQuantity();
        if (currentStock < quantity) {
            throw new IllegalArgumentException("Không đủ tồn kho để xuất.");
        }

        InventoryLog log = new InventoryLog();
        log.setAction("EXPORT");
        log.setProductDetail(productDetail);
        log.setQuantity(-quantity); // xuất là số âm
        log.setReferenceType("Order");
        log.setReferenceId(orderId);
        log.setCreatedAt(LocalDateTime.now());
        inventoryLogRepository.save(log);

        productDetail.setQuantity(currentStock - quantity);
        productDetailRepository.save(productDetail);
    }

    @Override
    @Transactional
    public void importProduct(Integer productDetailId, int quantity, Integer importInvoiceId, String note) {
        if (quantity <= 0) throw new IllegalArgumentException("Số lượng nhập phải > 0");

        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm"));

        InventoryLog log = new InventoryLog();
        log.setAction("IMPORT");
        log.setProductDetail(productDetail);
        log.setQuantity(quantity);
        log.setReferenceType("ImportInvoice");
        log.setReferenceId(importInvoiceId);
        log.setCreatedAt(LocalDateTime.now());
        inventoryLogRepository.save(log);

        productDetail.setQuantity(productDetail.getQuantity() + quantity);
        productDetailRepository.save(productDetail);

        // Bạn vẫn có thể log ghi chú ra console (tùy chọn)
        System.out.println("Ghi chú nhập hàng (bỏ qua DB): " + note);
    }

    @Override
    @Transactional
    public void adjustStock(Integer productDetailId, int quantityChange, String note, Long userId, Integer adjustmentId) {
        if (quantityChange == 0) throw new IllegalArgumentException("Điều chỉnh không thể bằng 0");

        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm"));

        InventoryLog log = new InventoryLog();
        log.setAction("ADJUSTMENT");
        log.setProductDetail(productDetail);
        log.setQuantity(quantityChange);
        log.setReferenceType("InventoryAdjustment");
        log.setReferenceId(adjustmentId);
        log.setCreatedAt(LocalDateTime.now());
        inventoryLogRepository.save(log);

        productDetail.setQuantity(productDetail.getQuantity() + quantityChange);
        productDetailRepository.save(productDetail);

        // Ghi chú kiểm kho có thể log ra nếu muốn
        System.out.println("Ghi chú điều chỉnh: " + note + ", bởi user: " + userId);
    }
}
