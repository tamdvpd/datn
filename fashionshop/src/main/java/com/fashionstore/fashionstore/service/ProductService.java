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

    // Lấy sản phẩm theo ID
    Optional<Product> getProductById(Integer id);

    // Tạo mới sản phẩm
    Product createProduct(Product product, MultipartFile imageFile);

    // Cập nhật sản phẩm với object Product (nội bộ hoặc từ client gửi JSON)
    Product updateProduct(Integer id, Product updatedProduct, MultipartFile imageFile);

    // Cập nhật sản phẩm với từng trường riêng lẻ (cho form submit hoặc FormData)
    Product updateProduct(Integer id, String name, String description, String brand, Boolean status, Integer categoryId,
            MultipartFile image);

    // Xoá sản phẩm
    void deleteProduct(Integer id);

    // Tìm kiếm sản phẩm theo tên, danh mục, khoảng giá
    List<ProductDetail> searchProducts(String name, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice);
}