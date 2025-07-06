package com.fashionstore.fashionstore.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fashionstore.fashionstore.entity.InventoryAdjustment;

public interface InventoryAdjustmentService {
    Page<InventoryAdjustment> getAllInventoryAdjustment(Pageable pageable);

    InventoryAdjustment createInventoryAdjustment(InventoryAdjustment inventoryAdjustment);

    InventoryAdjustment updateInventoryAdjustment(Integer id, InventoryAdjustment inventoryAdjustment);

    void deleteInventoryAdjustment(Integer id);
}
