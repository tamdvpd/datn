package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.InventoryLog;
import com.fashionstore.fashionstore.service.InventoryLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/inventoryLogs")
@RequiredArgsConstructor
public class InventoryLogController {

    private final InventoryLogService inventoryLogService;

    @GetMapping
    public ResponseEntity<List<InventoryLog>> getAll() {
        return ResponseEntity.ok(inventoryLogService.getAllInventoryLogs());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<InventoryLog>> getAllPaged(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(inventoryLogService.getAllInventoryLogs(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryLog> getById(@PathVariable Integer id) {
        return inventoryLogService.getInventoryLogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<InventoryLog> create(@RequestBody InventoryLog inventoryLog) {
        InventoryLog createdLog = inventoryLogService.createInventoryLog(inventoryLog);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryLog> update(@PathVariable Integer id, @RequestBody InventoryLog inventoryLog) {
        InventoryLog updatedLog = inventoryLogService.updateInventoryLog(id, inventoryLog);
        if (updatedLog != null) {
            return ResponseEntity.ok(updatedLog);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        inventoryLogService.deleteInventoryLog(id);
        return ResponseEntity.noContent().build();
    }
}