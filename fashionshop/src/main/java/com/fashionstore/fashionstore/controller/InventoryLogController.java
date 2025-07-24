package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.InventoryLog;
import com.fashionstore.fashionstore.service.InventoryLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/inventoryLogs")
@RequiredArgsConstructor
public class InventoryLogController {

    private final InventoryLogService inventoryLogService;

    /**
     * Lấy toàn bộ lịch sử nhập/xuất/điều chỉnh kho
     */
    @GetMapping
    public ResponseEntity<List<InventoryLog>> getAllLogs() {
        return ResponseEntity.ok(inventoryLogService.getAllInventoryLogs());
    }

    /**
     * Lấy 1 log theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventoryLog> getLogById(@PathVariable Integer id) {
        return inventoryLogService.getInventoryLogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Tính tồn kho hiện tại của 1 sản phẩm chi tiết
     */
    @GetMapping("/stock/{productDetailId}")
    public ResponseEntity<Integer> getCurrentStock(@PathVariable Integer productDetailId) {
        int stock = inventoryLogService.getCurrentStock(productDetailId);
        return ResponseEntity.ok(stock);
    }

    /**
     * Nhập hàng (IMPORT)
     */
    @PostMapping("/import")
    public ResponseEntity<String> importProduct(
            @RequestParam Integer productDetailId,
            @RequestParam int quantity,
            @RequestParam(required = false) Integer importInvoiceId,
            @RequestParam(required = false) String note
    ) {
        inventoryLogService.importProduct(productDetailId, quantity, importInvoiceId, note);
        return ResponseEntity.ok("Nhập hàng thành công");
    }

    /**
     * Xuất kho (EXPORT)
     */
    @PostMapping("/export")
    public ResponseEntity<String> exportProduct(
            @RequestParam Integer productDetailId,
            @RequestParam int quantity,
            @RequestParam(required = false) Integer orderId
    ) {
        inventoryLogService.exportProduct(productDetailId, quantity, orderId);
        return ResponseEntity.ok("Xuất hàng thành công");
    }

    /**
     * Điều chỉnh tồn kho thủ công (ADJUSTMENT)
     */
    @PostMapping("/adjust")
    public ResponseEntity<String> adjustStock(
            @RequestParam Integer productDetailId,
            @RequestParam int quantityChange,
            @RequestParam(required = false) String note,
            @RequestParam Long userId,
            @RequestParam(required = false) Integer adjustmentId
    ) {
        inventoryLogService.adjustStock(productDetailId, quantityChange, note, userId, adjustmentId);
        return ResponseEntity.ok("Điều chỉnh tồn kho thành công");
    }
}
