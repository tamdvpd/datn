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

    @GetMapping
    public ResponseEntity<List<ImportInvoiceDetail>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImportInvoiceDetail> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/by-invoice/{invoiceId}")
    public ResponseEntity<List<ImportInvoiceDetail>> getByImportInvoiceId(@PathVariable Integer invoiceId) {
        return ResponseEntity.ok(service.getByImportInvoiceId(invoiceId));
    }

    @PostMapping
    public ResponseEntity<ImportInvoiceDetail> create(@RequestBody ImportInvoiceDetail detail) {
        return ResponseEntity.ok(service.create(detail));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
