package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.ShippingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingLogRepository extends JpaRepository<ShippingLog, Integer> {
}