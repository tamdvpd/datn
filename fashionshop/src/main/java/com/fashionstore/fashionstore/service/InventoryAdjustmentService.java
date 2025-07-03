package com.fashionstore.fashionstore.service;

import java.util.List;

import com.fashionstore.fashionstore.entity.InventoryAdjustment;

public interface InventoryAdjustmentService {
    List<InventoryAdjustment> getAllInventoryAdjustment();

    InventoryAdjustment createInventoryAdjustment(InventoryAdjustment inventoryAdjustment);

    InventoryAdjustment updateInventoryAdjustment(Integer id, InventoryAdjustment inventoryAdjustment);

    void deleteInventoryAdjustment(Integer id);
}
