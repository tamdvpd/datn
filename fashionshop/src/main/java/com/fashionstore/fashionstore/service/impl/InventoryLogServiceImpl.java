package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.InventoryLog;
import com.fashionstore.fashionstore.repository.InventoryLogRepository;
import com.fashionstore.fashionstore.service.InventoryLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryLogServiceImpl implements InventoryLogService {

    private final InventoryLogRepository inventoryLogRepository;

    @Override
    public List<InventoryLog> getAllInventoryLogs() {
        return inventoryLogRepository.findAll();
    }

    @Override
    public Page<InventoryLog> getAllInventoryLogs(Pageable pageable) {
        return inventoryLogRepository.findAll(pageable);
    }

    @Override
    public Optional<InventoryLog> getInventoryLogById(Integer id) {
        return inventoryLogRepository.findById(id);
    }

    @Override
    public InventoryLog createInventoryLog(InventoryLog inventoryLog) {
        return inventoryLogRepository.save(inventoryLog);
    }

    @Override
    public InventoryLog updateInventoryLog(Integer id, InventoryLog inventoryLog) {
        // Tìm kiếm và cập nhật nếu tồn tại
        return inventoryLogRepository.findById(id).map(existingLog -> {
            existingLog.setAction(inventoryLog.getAction());
            existingLog.setQuantity(inventoryLog.getQuantity());
            existingLog.setReferenceType(inventoryLog.getReferenceType());
            existingLog.setReferenceId(inventoryLog.getReferenceId());
            // Nếu có thêm trường khác cần cập nhật, thêm vào đây
            return inventoryLogRepository.save(existingLog);
        }).orElseThrow(() -> new RuntimeException("Không tìm thấy InventoryLog với id: " + id));
    }

    @Override
    public void deleteInventoryLog(Integer id) {
        if (!inventoryLogRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy InventoryLog với id: " + id);
        }
        inventoryLogRepository.deleteById(id);
    }
}