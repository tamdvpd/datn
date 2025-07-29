package com.fashionstore.fashionstore.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fashionstore.fashionstore.entity.ProductDetail;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {

        List<ProductDetail> findByProductId(Integer productId);

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

}