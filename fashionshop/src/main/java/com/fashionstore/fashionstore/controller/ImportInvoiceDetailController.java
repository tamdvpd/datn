package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.ImportInvoiceDetail;
import com.fashionstore.fashionstore.service.ImportInvoiceDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/import-invoice-details")
@CrossOrigin(origins = "http://localhost:3001") // Điều chỉnh nếu frontend port khác
@RequiredArgsConstructor
public class ImportInvoiceDetailController {

    private final ImportInvoiceDetailService service;

    // ✅ Lấy toàn bộ chi tiết nhập
    @GetMapping
    public ResponseEntity<List<ImportInvoiceDetail>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ✅ Lấy chi tiết nhập theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ImportInvoiceDetail> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ✅ Lấy tất cả chi tiết theo mã phiếu nhập (xử lý ID truyền sai kiểu)
    @GetMapping("/by-invoice/{invoiceId}")
    public ResponseEntity<?> getByImportInvoiceId(@PathVariable String invoiceId) {
        try {
            // Trim để tránh lỗi khi có %0A hoặc xuống dòng (\n)
            Integer cleanId = Integer.parseInt(invoiceId.trim());
            List<ImportInvoiceDetail> details = service.getByImportInvoiceId(cleanId);
            return ResponseEntity.ok(details);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invoice ID không hợp lệ: " + invoiceId);
        }
    }

    // ✅ Tạo mới chi tiết nhập
    @PostMapping
    public ResponseEntity<ImportInvoiceDetail> create(@RequestBody ImportInvoiceDetail detail) {
        return ResponseEntity.ok(service.create(detail));
    }

    // ✅ Xoá chi tiết nhập theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
