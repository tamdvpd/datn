package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN p.productDetails d " +
            "WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
            "AND (:minPrice IS NULL OR (d.price >= :minPrice OR d.discountPrice >= :minPrice)) " +
            "AND (:maxPrice IS NULL OR (d.price <= :maxPrice OR d.discountPrice <= :maxPrice))")
    Page<Product> searchProducts(@Param("name") String name,
            @Param("categoryId") Integer categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);

    // Lấy tất cả sản phẩm đang hoạt động (status = true)
    Page<Product> findByStatusTrue(Pageable pageable);
}
