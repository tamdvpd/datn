package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.InventoryLog;

import java.util.List;
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
}
