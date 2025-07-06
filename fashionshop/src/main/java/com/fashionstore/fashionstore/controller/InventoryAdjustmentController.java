package com.fashionstore.fashionstore.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fashionstore.fashionstore.entity.InventoryAdjustment;
import com.fashionstore.fashionstore.service.InventoryAdjustmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/api/inventory_adjustment")
@RequiredArgsConstructor
public class InventoryAdjustmentController {

    private final InventoryAdjustmentService inventoryAdjustmentService;

    @GetMapping
    public ResponseEntity<Page<InventoryAdjustment>> getAll(@RequestParam(defaultValue = "0") int page) {
        int pageSize = 6;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<InventoryAdjustment> result = inventoryAdjustmentService.getAllInventoryAdjustment(pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<InventoryAdjustment> create(@Valid @RequestBody InventoryAdjustment inventoryAdjustment) {
        return ResponseEntity.status(201)
                .body(inventoryAdjustmentService.createInventoryAdjustment(inventoryAdjustment));
    }

    @PutMapping("{id}")
    public ResponseEntity<InventoryAdjustment> update(@PathVariable Integer id,
            @Valid @RequestBody InventoryAdjustment inventoryAdjustment) {
        return ResponseEntity.status(201)
                .body(inventoryAdjustmentService.updateInventoryAdjustment(id, inventoryAdjustment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        inventoryAdjustmentService.deleteInventoryAdjustment(id);
        return ResponseEntity.noContent().build();
    }

}
