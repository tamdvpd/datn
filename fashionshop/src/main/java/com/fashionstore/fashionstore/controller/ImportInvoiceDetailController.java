package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.ImportInvoiceDetail;
import com.fashionstore.fashionstore.service.ImportInvoiceDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/import-invoice-details")
@CrossOrigin(origins = "http://localhost:3001")
@RequiredArgsConstructor
public class ImportInvoiceDetailController {

    private final ImportInvoiceDetailService service;

    /** Lấy tất cả chi tiết phiếu nhập */
    @GetMapping
    public ResponseEntity<List<ImportInvoiceDetail>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    /** Lấy chi tiết phiếu nhập theo ID chi tiết */
    @GetMapping("/{id}")
    public ResponseEntity<ImportInvoiceDetail> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    /** Lấy danh sách chi tiết theo ID phiếu nhập */
    @GetMapping("/by-invoice/{invoiceId}")
    public ResponseEntity<List<ImportInvoiceDetail>> getByImportInvoiceId(@PathVariable Integer invoiceId) {
        return ResponseEntity.ok(service.getByImportInvoiceId(invoiceId));
    }

    /** Thêm sản phẩm vào phiếu nhập và cập nhật tồn kho */
    @PostMapping
    public ResponseEntity<ImportInvoiceDetail> create(@RequestBody ImportInvoiceDetail detail) {
        try {
            ImportInvoiceDetail created = service.create(detail); // service đã tự cộng kho
            return ResponseEntity.ok(created);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    /** Cập nhật chi tiết sản phẩm trong phiếu nhập và cập nhật tồn kho */
    @PutMapping("/{id}")
    public ResponseEntity<ImportInvoiceDetail> update(@PathVariable Integer id,
            @RequestBody ImportInvoiceDetail detail) {
        try {
            ImportInvoiceDetail updated = service.update(id, detail); // service đã tự tính chênh lệch kho
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /** Xóa chi tiết sản phẩm trong phiếu nhập và trừ tồn kho */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            service.delete(id); // service đã tự trừ kho
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /** Lấy tổng tiền của phiếu nhập (nếu cần hiển thị) */
    @GetMapping("/total/{invoiceId}")
    public ResponseEntity<Double> getTotalByInvoice(@PathVariable Integer invoiceId) {
        return ResponseEntity.ok(service.getTotalAmountByInvoice(invoiceId));
    }
}
