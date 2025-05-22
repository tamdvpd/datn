package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductDetailService {
    List<ProductDetail> getAllProductDetails();
    Page<ProductDetail> getAllProductDetails(Pageable pageable);
    Optional<ProductDetail> getProductDetailById(Integer id);
    ProductDetail createProductDetail(ProductDetail productDetail);
    ProductDetail updateProductDetail(Integer id, ProductDetail productDetail);
    void deleteProductDetail(Integer id);
}