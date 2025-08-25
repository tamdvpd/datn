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

    // Tạo mới phiếu nhập (có thể kèm nhà cung cấp mới và chi tiết)
    @PostMapping
    public ResponseEntity<ImportInvoice> create(@RequestBody ImportInvoice invoice) {
        ImportInvoice created = importInvoiceService.createImportInvoice(invoice);
        return ResponseEntity.ok(created);
    }

    // Cập nhật phiếu nhập
    @PutMapping("/{id}")
    public ResponseEntity<ImportInvoice> update(@PathVariable Integer id, @Valid @RequestBody ImportInvoice invoice) {
        Optional<ImportInvoice> existing = importInvoiceService.getImportInvoiceById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        invoice.setId(id);

        // Thiết lập lại quan hệ 2 chiều với chi tiết nếu có
        if (invoice.getImportInvoiceDetails() != null) {
            invoice.getImportInvoiceDetails().forEach(detail -> detail.setImportInvoice(invoice));
        }

        ImportInvoice updated = importInvoiceService.updateImportInvoice(id, invoice);
        return ResponseEntity.ok(updated);
    }

    // Nhập kho cho phiếu nhập
    @PostMapping("/{id}/import-stock")
    public ResponseEntity<?> importStock(@PathVariable Integer id) {
        importInvoiceService.importStock(id);
        return ResponseEntity.ok("Nhập kho thành công");
    }

    // Xóa phiếu nhập
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Optional<ImportInvoice> existing = importInvoiceService.getImportInvoiceById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        importInvoiceService.deleteImportInvoice(id);
        return ResponseEntity.noContent().build();
    }
}
