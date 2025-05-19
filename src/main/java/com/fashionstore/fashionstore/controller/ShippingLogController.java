package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.ShippingLog;
import com.fashionstore.fashionstore.service.ShippingLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shippingLogs")
@RequiredArgsConstructor
public class ShippingLogController {

    private final ShippingLogService shippingLogService;

    @GetMapping
    public ResponseEntity<List<ShippingLog>> getAll() {
        return ResponseEntity.ok(shippingLogService.getAllShippingLogs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShippingLog> getById(@PathVariable Integer id) {
        return ResponseEntity.of(shippingLogService.getShippingLogById(id));
    }

    @PostMapping
    public ResponseEntity<ShippingLog> create(@RequestBody ShippingLog shippingLog) {
        return ResponseEntity.ok(shippingLogService.createShippingLog(shippingLog));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShippingLog> update(@PathVariable Integer id, @RequestBody ShippingLog shippingLog) {
        return ResponseEntity.ok(shippingLogService.updateShippingLog(id, shippingLog));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        shippingLogService.deleteShippingLog(id);
        return ResponseEntity.noContent().build();
    }
}