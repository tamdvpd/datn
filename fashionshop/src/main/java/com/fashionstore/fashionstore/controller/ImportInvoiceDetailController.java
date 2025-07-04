package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.ImportInvoiceDetail;
import com.fashionstore.fashionstore.service.ImportInvoiceDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/import-invoice-details")
@RequiredArgsConstructor
public class ImportInvoiceDetailController {

    private final ImportInvoiceDetailService importInvoiceDetailService;

    //  API thêm hoặc cập nhật 1 dòng chi tiết
    @PostMapping
    public ResponseEntity<ImportInvoiceDetail> createOrUpdate(@RequestBody ImportInvoiceDetail detail) {
        ImportInvoiceDetail saved = importInvoiceDetailService.save(detail);
        return ResponseEntity.ok(saved);
    }

    // API lấy danh sách dòng chi tiết theo mã phiếu nhập
    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<ImportInvoiceDetail>> getByInvoice(@PathVariable Integer invoiceId) {
        List<ImportInvoiceDetail> details = importInvoiceDetailService.getByImportInvoiceId(invoiceId);
        return ResponseEntity.ok(details);
    }

    // API xoá dòng chi tiết theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        importInvoiceDetailService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
