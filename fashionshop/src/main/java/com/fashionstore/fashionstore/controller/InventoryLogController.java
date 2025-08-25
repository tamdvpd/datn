package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.InventoryLog;
import com.fashionstore.fashionstore.service.InventoryLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin/inventoryLogs")
@RequiredArgsConstructor
public class InventoryLogController {

    private final InventoryLogService inventoryLogService;

    /** Lấy toàn bộ lịch sử nhập/xuất/điều chỉnh kho */
    @GetMapping
    public ResponseEntity<List<InventoryLog>> getAllLogs() {
        return ResponseEntity.ok(inventoryLogService.getAllInventoryLogs());
    }

    /** Lấy 1 log theo ID */
    @GetMapping("/{id}")
    public ResponseEntity<InventoryLog> getLogById(@PathVariable Integer id) {
        return inventoryLogService.getInventoryLogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Tính tồn kho hiện tại của 1 sản phẩm chi tiết */
    @GetMapping("/stock/{productDetailId}")
    public ResponseEntity<Integer> getCurrentStock(@PathVariable Integer productDetailId) {
        int stock = inventoryLogService.getCurrentStock(productDetailId);
        return ResponseEntity.ok(stock);
    }

    /** Nhập kho (IMPORT) */
    @PostMapping("/import")
    public ResponseEntity<String> importProduct(@RequestBody Map<String, Object> payload) {
        Integer productDetailId = (Integer) payload.get("productDetailId");
        Integer quantity = (Integer) payload.get("quantity");
        Integer importInvoiceId = (Integer) payload.get("importInvoiceId");
        String note = (String) payload.get("note");

        if (productDetailId == null || quantity == null || quantity <= 0)
            return ResponseEntity.badRequest().body("Thông tin nhập kho không hợp lệ");

        inventoryLogService.importProduct(productDetailId, quantity, importInvoiceId, note);
        return ResponseEntity.ok("Nhập hàng thành công");
    }

    /** Xuất kho (EXPORT) */
    @PostMapping("/export")
    public ResponseEntity<String> exportProduct(@RequestBody Map<String, Object> payload) {
        Integer productDetailId = (Integer) payload.get("productDetailId");
        Integer quantity = (Integer) payload.get("quantity");
        Integer orderId = (Integer) payload.get("orderId");

        if (productDetailId == null || quantity == null || quantity <= 0)
            return ResponseEntity.badRequest().body("Thông tin xuất kho không hợp lệ");

        inventoryLogService.exportProduct(productDetailId, quantity, orderId);
        return ResponseEntity.ok("Xuất hàng thành công");
    }

    /** Điều chỉnh tồn kho thủ công (ADJUSTMENT) */
    @PostMapping("/adjust")
    public ResponseEntity<String> adjustStock(@RequestBody Map<String, Object> payload) {
        Integer productDetailId = (Integer) payload.get("productDetailId");
        Integer quantityChange = (Integer) payload.get("quantityChange");
        String note = (String) payload.get("note");
        Long userId = payload.get("userId") instanceof Integer ? ((Integer) payload.get("userId")).longValue()
                : (Long) payload.get("userId");
        Integer adjustmentId = (Integer) payload.get("adjustmentId");

        if (productDetailId == null || quantityChange == null || userId == null)
            return ResponseEntity.badRequest().body("Thông tin điều chỉnh không hợp lệ");

        inventoryLogService.adjustStock(productDetailId, quantityChange, note, userId, adjustmentId);
        return ResponseEntity.ok("Điều chỉnh tồn kho thành công");
    }

    /**
     * Lấy danh sách sản phẩm + tồn kho hiện tại (bảng kho hàng) với filter &
     * pagination
     */
    @GetMapping("/warehouse")
    public ResponseEntity<Map<String, Object>> getWarehouseStock(
            @RequestParam(required = false) String product,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) Integer stockMin,
            @RequestParam(required = false) Integer stockMax,
            @RequestParam(required = false) Double priceMin,
            @RequestParam(required = false) Double priceMax,
            @RequestParam(required = false) Double discountMin,
            @RequestParam(required = false) Double discountMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int sizePage) {
        Page<Object[]> pageResult = inventoryLogService.getWarehouseStockWithFilters(
                product, color, size,
                stockMin, stockMax,
                priceMin, priceMax,
                discountMin, discountMax,
                PageRequest.of(page, sizePage));

        List<Map<String, Object>> data = new ArrayList<>();
        for (Object[] row : pageResult.getContent()) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("productId", row[0]);
            map.put("productName", row[1]);
            map.put("productDetailId", row[2]);
            map.put("color", row[3]);
            map.put("size", row[4]);
            map.put("currentStock", row[5]);
            map.put("price", row[6]);
            map.put("discountPrice", row[7]);
            data.add(map);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", data);
        response.put("currentPage", pageResult.getNumber());
        response.put("totalItems", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());

        return ResponseEntity.ok(response);
    }

    /** Lấy danh sách các bộ lọc (colors, sizes) */
    @GetMapping("/warehouse/filters")
    public ResponseEntity<Map<String, List<String>>> getWarehouseFilters() {
        return ResponseEntity.ok(inventoryLogService.getFilterOptions());
    }
}
