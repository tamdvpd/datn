package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.Product;
import com.fashionstore.fashionstore.entity.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {

        @Query("SELECT pd FROM ProductDetail pd " +
                        "JOIN pd.product p " +
                        "WHERE (:name IS NULL OR p.name LIKE %:name%) " +
                        "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
                        "AND (:minPrice IS NULL OR pd.price >= :minPrice) " +
                        "AND (:maxPrice IS NULL OR pd.price <= :maxPrice)")
        List<ProductDetail> searchProducts(
                        @Param("name") String name,
                        @Param("categoryId") Integer categoryId,
                        @Param("minPrice") BigDecimal minPrice,
                        @Param("maxPrice") BigDecimal maxPrice);

        @Query("SELECT DISTINCT pd.color FROM ProductDetail pd WHERE pd.color IS NOT NULL AND pd.color <> ''")
        List<String> findDistinctColors();

        @Query("SELECT DISTINCT pd.size FROM ProductDetail pd WHERE pd.size IS NOT NULL AND pd.size <> ''")
        List<String> findDistinctSizes();

        @Query("""
                        SELECT p.id AS productId, p.name AS productName, pd.id AS productDetailId,
                               pd.color, pd.size, pd.quantity AS currentStock, pd.price, pd.discountPrice
                        FROM ProductDetail pd
                        JOIN pd.product p
                        WHERE (:productName IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')))
                          AND (:color IS NULL OR LOWER(pd.color) = LOWER(:color))
                          AND (:size IS NULL OR LOWER(pd.size) = LOWER(:size))
                          AND (:priceMin IS NULL OR pd.price >= :priceMin)
                          AND (:priceMax IS NULL OR pd.price <= :priceMax)
                          AND (:stockMin IS NULL OR pd.quantity >= :stockMin)
                          AND (:stockMax IS NULL OR pd.quantity <= :stockMax)
                          AND (:discountMin IS NULL OR ((pd.price - COALESCE(pd.discountPrice, pd.price)) * 100 / pd.price) >= :discountMin)
                          AND (:discountMax IS NULL OR ((pd.price - COALESCE(pd.discountPrice, pd.price)) * 100 / pd.price) <= :discountMax)
                        """)
        Page<Object[]> getWarehouseStockWithFilters(
                        @Param("productName") String productName,
                        @Param("color") String color,
                        @Param("size") String size,
                        @Param("stockMin") Integer stockMin,
                        @Param("stockMax") Integer stockMax,
                        @Param("priceMin") BigDecimal priceMin,
                        @Param("priceMax") BigDecimal priceMax,
                        @Param("discountMin") Integer discountMin,
                        @Param("discountMax") Integer discountMax,
                        Pageable pageable);

        @Query("""
                        SELECT p.id AS productId, p.name AS productName, pd.id AS productDetailId,
                               pd.color, pd.size, pd.quantity AS currentStock, pd.price, pd.discountPrice
                        FROM ProductDetail pd
                        JOIN pd.product p
                        """)

        List<Object[]> findAllCurrentStocks();

        List<ProductDetail> findByProductId(Integer productId);
}