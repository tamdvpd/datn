package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.ImportInvoice;
import com.fashionstore.fashionstore.service.ImportInvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/api/import-invoices")
@RequiredArgsConstructor
public class ImportInvoiceController {

    private final ImportInvoiceService importInvoiceService;

    // Lấy tất cả phiếu nhập
    @GetMapping
    public ResponseEntity<List<ImportInvoice>> getAll() {
        return ResponseEntity.ok(importInvoiceService.getAllImportInvoices());
    }

    // Lấy phiếu nhập theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ImportInvoice> getById(@PathVariable Integer id) {
        Optional<ImportInvoice> invoice = importInvoiceService.getImportInvoiceById(id);
        return invoice.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Tạo mới phiếu nhập (có chi tiết)
    @PostMapping
    public ResponseEntity<ImportInvoice> create(@Valid @RequestBody ImportInvoice invoice) {
        // Thiết lập quan hệ 2 chiều
        if (invoice.getImportInvoiceDetails() != null) {
            invoice.getImportInvoiceDetails().forEach(detail -> detail.setImportInvoice(invoice));
        }
        ImportInvoice created = importInvoiceService.createImportInvoice(invoice);
        return ResponseEntity.status(201).body(created);
    }

    // Cập nhật phiếu nhập
    @PutMapping("/{id}")
    public ResponseEntity<ImportInvoice> update(@PathVariable Integer id, @Valid @RequestBody ImportInvoice invoice) {
        if (importInvoiceService.getImportInvoiceById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        invoice.setId(id);

        // Thiết lập lại quan hệ 2 chiều
        if (invoice.getImportInvoiceDetails() != null) {
            invoice.getImportInvoiceDetails().forEach(detail -> detail.setImportInvoice(invoice));
        }

        ImportInvoice updated = importInvoiceService.updateImportInvoice(id, invoice);
        return ResponseEntity.ok(updated);
    }

    // Xóa phiếu nhập
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (importInvoiceService.getImportInvoiceById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        importInvoiceService.deleteImportInvoice(id);
        return ResponseEntity.noContent().build();
    }
}