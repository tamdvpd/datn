package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.InventoryLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface InventoryLogService {
    List<InventoryLog> getAllInventoryLogs();
    Page<InventoryLog> getAllInventoryLogs(Pageable pageable);
    Optional<InventoryLog> getInventoryLogById(Integer id);
    InventoryLog createInventoryLog(InventoryLog inventoryLog);
    InventoryLog updateInventoryLog(Integer id, InventoryLog inventoryLog);
    void deleteInventoryLog(Integer id);
}