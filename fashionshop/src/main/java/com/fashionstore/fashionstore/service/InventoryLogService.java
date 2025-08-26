package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.InventoryLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface InventoryLogService {

    List<InventoryLog> getAllInventoryLogs();

    Optional<InventoryLog> getInventoryLogById(Integer id);

    InventoryLog createInventoryLog(InventoryLog log);

    /**
     * Tính tồn kho hiện tại của 1 sản phẩm chi tiết dựa vào log
     */
    int getCurrentStock(Integer productDetailId);

    /**
     * Ghi log xuất hàng và cập nhật tồn kho
     */
    void exportProduct(Integer productDetailId, int quantity, Integer orderId);

    /**
     * Ghi log nhập hàng và cập nhật tồn kho
     */
    void importProduct(Integer productDetailId, int quantity, Integer importInvoiceId, String note);

    /**
     * Ghi log điều chỉnh thủ công (khi kiểm kho)
     */
    void adjustStock(Integer productDetailId, int quantityChange, String note, Long userId, Integer adjustmentId);

    /**
     * Lấy danh sách tồn kho (product + color + size + tồn) - không filter
     */
    List<Object[]> getAllCurrentStocks();

    /**
     * Lấy danh sách tồn kho với filter + pagination
     */
    Page<Object[]> getWarehouseStockWithFilters(
            String product, String color, String size,
            Integer stockMin, Integer stockMax,
            Double priceMin, Double priceMax,
            Double discountMin, Double discountMax,
            Pageable pageable);

    Map<String, List<String>> getFilterOptions();

    
}