package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.ShippingLog;
import com.fashionstore.fashionstore.repository.ShippingLogRepository;
import com.fashionstore.fashionstore.service.ShippingLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShippingLogServiceImpl implements ShippingLogService {

    private final ShippingLogRepository shippingLogRepository;

    @Override
    public List<ShippingLog> getAllShippingLogs() {
        return shippingLogRepository.findAll();
    }

    @Override
    public Page<ShippingLog> getAllShippingLogs(Pageable pageable) {
        return shippingLogRepository.findAll(pageable);
    }

    @Override
    public Optional<ShippingLog> getShippingLogById(Integer id) {
        return shippingLogRepository.findById(id);
    }

    @Override
    public ShippingLog createShippingLog(ShippingLog shippingLog) {
        return shippingLogRepository.save(shippingLog);
    }

    @Override
    public ShippingLog updateShippingLog(Integer id, ShippingLog shippingLog) {
        shippingLog.setId(id);
        return shippingLogRepository.save(shippingLog);
    }

    @Override
    public void deleteShippingLog(Integer id) {
        shippingLogRepository.deleteById(id);
    }
}