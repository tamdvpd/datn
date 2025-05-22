package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.InventoryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryLogRepository extends JpaRepository<InventoryLog, Integer> {
}