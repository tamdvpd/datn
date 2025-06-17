package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.Product;
import com.fashionstore.fashionstore.entity.ProductDetail;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {

    // ✅ Trả về danh sách ProductDetail, không cần static!
    @Query("SELECT pd FROM ProductDetail pd WHERE " +
           "(:name IS NULL OR LOWER(pd.product.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:categoryId IS NULL OR pd.product.category.id = :categoryId) AND " +
           "(:minPrice IS NULL OR pd.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR pd.price <= :maxPrice)")
    List<ProductDetail> searchProducts(
        @Param("name") String name,
        @Param("categoryId") Integer categoryId,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice
    );

    // ✅ Đảm bảo đúng hàm để tìm detail theo Product ID
    Optional<ProductDetail> findFirstByProduct_Id(Integer productId);
}

