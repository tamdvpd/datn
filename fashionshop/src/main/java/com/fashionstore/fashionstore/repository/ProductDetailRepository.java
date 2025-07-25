package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.Product;
import com.fashionstore.fashionstore.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {

    /**
     * Tìm kiếm sản phẩm theo tên, danh mục, khoảng giá.
     */
    @Query("SELECT DISTINCT pd.product FROM ProductDetail pd WHERE " +
            "(:name IS NULL OR LOWER(pd.product.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:categoryId IS NULL OR pd.product.category.id = :categoryId) AND " +
            "(:minPrice IS NULL OR pd.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR pd.price <= :maxPrice)")
    List<Product> searchProducts(
            @Param("name") String name,
            @Param("categoryId") Integer categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice);

    /**
     * Lấy danh sách màu sắc duy nhất từ ProductDetail.
     */
    @Query("SELECT DISTINCT pd.color FROM ProductDetail pd WHERE pd.color IS NOT NULL AND pd.color <> ''")
    List<String> findDistinctColors();

    /**
     * Lấy danh sách kích cỡ duy nhất từ ProductDetail.
     */
    @Query("SELECT DISTINCT pd.size FROM ProductDetail pd WHERE pd.size IS NOT NULL AND pd.size <> ''")
    List<String> findDistinctSizes();
}
