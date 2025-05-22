package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.InventoryLog;
import com.fashionstore.fashionstore.service.InventoryLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<InventoryLog> getById(@PathVariable Integer id) {
        return ResponseEntity.of(inventoryLogService.getInventoryLogById(id));
    }

    @PostMapping
    public ResponseEntity<InventoryLog> create(@RequestBody InventoryLog inventoryLog) {
        return ResponseEntity.ok(inventoryLogService.createInventoryLog(inventoryLog));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryLog> update(@PathVariable Integer id, @RequestBody InventoryLog inventoryLog) {
        return ResponseEntity.ok(inventoryLogService.updateInventoryLog(id, inventoryLog));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        inventoryLogService.deleteInventoryLog(id);
        return ResponseEntity.noContent().build();
    }
}
