package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.InventoryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryLogRepository extends JpaRepository<InventoryLog, Integer> {

    /**
     * Tính tổng tồn kho (số lượng) hiện tại của một sản phẩm chi tiết.
     */
    @Query("SELECT COALESCE(SUM(il.quantity), 0) FROM InventoryLog il WHERE il.productDetail.id = :productDetailId")
    int sumQuantityByProductDetailId(@Param("productDetailId") Integer productDetailId);

    /**
     * Lấy toàn bộ log theo sản phẩm chi tiết.
     */
    List<InventoryLog> findByProductDetail_Id(Integer productDetailId); // ✅ Sửa đúng

    /**
     * Lấy log theo action (IMPORT / EXPORT / ADJUSTMENT).
     */
    List<InventoryLog> findByAction(String action);

    /**
     * Lấy log theo sản phẩm chi tiết và loại action.
     */
    List<InventoryLog> findByProductDetail_IdAndAction(Integer productDetailId, String action); // ✅ Sửa đúng
}
