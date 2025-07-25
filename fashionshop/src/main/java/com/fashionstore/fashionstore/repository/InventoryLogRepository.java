package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.InventoryLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryLogRepository extends JpaRepository<InventoryLog, Integer> {

    /**
<<<<<<< HEAD
     * Tính tổng tồn kho (số lượng) hiện tại của một sản phẩm chi tiết.
=======
     * Tính tổng tồn kho hiện tại của một ProductDetail.
>>>>>>> origin/master
     */
    @Query("SELECT COALESCE(SUM(il.quantity), 0) FROM InventoryLog il WHERE il.productDetail.id = :productDetailId")
    int sumQuantityByProductDetailId(@Param("productDetailId") Integer productDetailId);

    /**
<<<<<<< HEAD
     * Lấy toàn bộ log theo sản phẩm chi tiết.
     */
    List<InventoryLog> findByProductDetail_Id(Integer productDetailId); // ✅ Sửa đúng

    /**
     * Lấy log theo action (IMPORT / EXPORT / ADJUSTMENT).
=======
     * Lấy tất cả log theo productDetailId.
     */
    List<InventoryLog> findByProductDetail_Id(Integer productDetailId);

    /**
     * Lấy log theo action (IMPORT, EXPORT, ADJUSTMENT).
>>>>>>> origin/master
     */
    List<InventoryLog> findByAction(String action);

    /**
<<<<<<< HEAD
     * Lấy log theo sản phẩm chi tiết và loại action.
     */
    List<InventoryLog> findByProductDetail_IdAndAction(Integer productDetailId, String action); // ✅ Sửa đúng
=======
     * Lấy log theo productDetailId và action.
     */
    List<InventoryLog> findByProductDetail_IdAndAction(Integer productDetailId, String action);

    /**
     * Lấy danh sách tồn kho hiện tại có filter và phân trang.
     */
    @Query(value = """
        SELECT 
            p.id AS product_id,
            p.name AS product_name,
            pd.id AS product_detail_id,
            pd.color,
            pd.size,
            ISNULL(SUM(il.quantity), 0) AS current_stock,
            pd.price,
            pd.discount_price
        FROM ProductDetails pd
        JOIN Products p ON p.id = pd.product_id
        LEFT JOIN InventoryLogs il ON il.product_detail_id = pd.id
        WHERE (:product IS NULL OR p.name LIKE %:product%)
          AND (:color IS NULL OR pd.color LIKE %:color%)
          AND (:size IS NULL OR pd.size LIKE %:size%)
          AND (:stockMin IS NULL OR pd.quantity >= :stockMin)
          AND (:stockMax IS NULL OR pd.quantity <= :stockMax)
          AND (:priceMin IS NULL OR pd.price >= :priceMin)
          AND (:priceMax IS NULL OR pd.price <= :priceMax)
          AND (:discountMin IS NULL OR pd.discount_price >= :discountMin)
          AND (:discountMax IS NULL OR pd.discount_price <= :discountMax)
        GROUP BY p.id, p.name, pd.id, pd.color, pd.size, pd.price, pd.discount_price
        """,
        countQuery = """
        SELECT COUNT(*) FROM ProductDetails pd
        JOIN Products p ON p.id = pd.product_id
        WHERE (:product IS NULL OR p.name LIKE %:product%)
          AND (:color IS NULL OR pd.color LIKE %:color%)
          AND (:size IS NULL OR pd.size LIKE %:size%)
          AND (:stockMin IS NULL OR pd.quantity >= :stockMin)
          AND (:stockMax IS NULL OR pd.quantity <= :stockMax)
          AND (:priceMin IS NULL OR pd.price >= :priceMin)
          AND (:priceMax IS NULL OR pd.price <= :priceMax)
          AND (:discountMin IS NULL OR pd.discount_price >= :discountMin)
          AND (:discountMax IS NULL OR pd.discount_price <= :discountMax)
        """,
        nativeQuery = true)
    Page<Object[]> getWarehouseStockWithFilters(
            @Param("product") String product,
            @Param("color") String color,
            @Param("size") String size,
            @Param("stockMin") Integer stockMin,
            @Param("stockMax") Integer stockMax,
            @Param("priceMin") Double priceMin,
            @Param("priceMax") Double priceMax,
            @Param("discountMin") Double discountMin,
            @Param("discountMax") Double discountMax,
            Pageable pageable
    );

    /**
     * Lấy danh sách tồn kho toàn bộ (không phân trang).
     */
    @Query(value = """
        SELECT 
            p.id AS product_id,
            p.name AS product_name,
            pd.id AS product_detail_id,
            pd.color,
            pd.size,
            ISNULL(SUM(il.quantity), 0) AS current_stock,
            pd.price,
            pd.discount_price
        FROM ProductDetails pd
        JOIN Products p ON p.id = pd.product_id
        LEFT JOIN InventoryLogs il ON il.product_detail_id = pd.id
        GROUP BY p.id, p.name, pd.id, pd.color, pd.size, pd.price, pd.discount_price
        """, nativeQuery = true)
    List<Object[]> getAllCurrentStocks();
>>>>>>> origin/master
}
