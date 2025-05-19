package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.ShippingLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ShippingLogService {
    List<ShippingLog> getAllShippingLogs();
    Page<ShippingLog> getAllShippingLogs(Pageable pageable);
    Optional<ShippingLog> getShippingLogById(Integer id);
    ShippingLog createShippingLog(ShippingLog shippingLog);
    ShippingLog updateShippingLog(Integer id, ShippingLog shippingLog);
    void deleteShippingLog(Integer id);
}