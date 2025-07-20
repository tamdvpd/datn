package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.ImportInvoiceDetail;
import com.fashionstore.fashionstore.service.ImportInvoiceDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/api/import-invoice-details")
@RequiredArgsConstructor
public class ImportInvoiceDetailController {

    private final ImportInvoiceDetailService importInvoiceDetailService;

    // Lấy tất cả dòng chi tiết
    @GetMapping
    public ResponseEntity<List<ImportInvoiceDetail>> getAll() {
        List<ImportInvoiceDetail> details = importInvoiceDetailService.findAll();
        if (!details.isEmpty()) {
            System.out.println("Trả về class: " + details.get(0).getClass().getName());
        }
        return ResponseEntity.ok(details);
    }

    // Thêm hoặc cập nhật dòng chi tiết
    @PostMapping
    public ResponseEntity<?> createOrUpdate(@RequestBody ImportInvoiceDetail detail) {
        if (detail.getImportInvoice() == null || detail.getImportInvoice().getId() == null) {
            return ResponseEntity.badRequest().body("❌ Thiếu thông tin phiếu nhập!");
        }
        if (detail.getProductDetail() == null || detail.getProductDetail().getId() == null) {
            return ResponseEntity.badRequest().body("❌ Thiếu biến thể sản phẩm!");
        }
        if (detail.getQuantity() == null || detail.getQuantity() <= 0) {
            return ResponseEntity.badRequest().body("❌ Số lượng phải lớn hơn 0!");
        }
        if (detail.getUnitPrice() == null || detail.getUnitPrice().doubleValue() < 0) {
            return ResponseEntity.badRequest().body("❌ Đơn giá không hợp lệ!");
        }

        ImportInvoiceDetail saved = importInvoiceDetailService.save(detail);
        return ResponseEntity.ok(saved);
    }

    // Lấy chi tiết theo mã phiếu nhập
    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<ImportInvoiceDetail>> getByInvoice(@PathVariable Integer invoiceId) {
        List<ImportInvoiceDetail> details = importInvoiceDetailService.getByImportInvoiceId(invoiceId);
        return ResponseEntity.ok(details);
    }

    // Xoá theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        try {
            importInvoiceDetailService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(404).body("❌ Không tìm thấy dòng chi tiết với ID: " + id);
        }
    }
}
