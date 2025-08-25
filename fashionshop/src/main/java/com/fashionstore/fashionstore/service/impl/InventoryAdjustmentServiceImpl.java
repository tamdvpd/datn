package com.fashionstore.fashionstore.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<InventoryAdjustment> getAllInventoryAdjustment(Pageable pageable) {
        return inventoryAdjustmentRepository.findAll(pageable);
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