package com.fashionstore.fashionstore.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fashionstore.fashionstore.entity.InventoryAdjustment;
import com.fashionstore.fashionstore.repository.InventoryAdjustmentRepository;
import com.fashionstore.fashionstore.service.InventoryAdjustmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryAdjustmentServiceImpl implements InventoryAdjustmentService {

    private final InventoryAdjustmentRepository inventoryAdjustmentRepository;

    @Override
    public List<InventoryAdjustment> getAllInventoryAdjustment() {
        return inventoryAdjustmentRepository.findAll();
    }

    @Override
    public InventoryAdjustment createInventoryAdjustment(InventoryAdjustment inventoryAdjustment) {
        return inventoryAdjustmentRepository.save(inventoryAdjustment);
    }

    @Override
    public InventoryAdjustment updateInventoryAdjustment(Integer id, InventoryAdjustment inventoryAdjustment) {
        return inventoryAdjustmentRepository.save(inventoryAdjustment);
    }

    @Override
    public void deleteInventoryAdjustment(Integer id) {
        inventoryAdjustmentRepository.deleteById(id);
    }

}
