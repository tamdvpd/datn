package com.fashionstore.fashionstore.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.fashionstore.fashionstore.entity.Product;
import com.fashionstore.fashionstore.entity.ProductDetail;

public interface ProductService {

    // Lấy tất cả sản phẩm
    List<Product> getAllProducts();

    // Lấy sản phẩm phân trang
    Page<Product> getAllProducts(Pageable pageable);

    Optional<Product> getProductById(Integer id);

    Product createProduct(Product product, MultipartFile imageFile);

    Product updateProduct(Integer id, Product updatedProduct, MultipartFile imageFile);

    Product updateProduct(Integer id, String name, String description, String brand,
            Boolean status, Integer categoryId, MultipartFile image);

    // Xoá sản phẩm
    void deleteProduct(Integer id);

    Page<Product> searchProducts(String keyword,
            Integer categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String sortBy,
            String order,
            int page, int size);

    List<ProductDetail> searchProducts(String name,
            Integer categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice);

    List<Product> getBestSellers(int limit);

}
