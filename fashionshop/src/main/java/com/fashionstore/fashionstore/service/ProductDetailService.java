package com.fashionstore.fashionstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.fashionstore.fashionstore.entity.ProductDetail;

public interface ProductDetailService {
    List<ProductDetail> getAllProductDetails();

    Optional<ProductDetail> getProductDetailById(Integer id);

    ProductDetail createProductDetail(ProductDetail productDetail, MultipartFile imageFile);

    ProductDetail updateProductDetail(Integer id, ProductDetail productDetail, MultipartFile imageFile);

    void deleteProductDetail(Integer id);

    List<ProductDetail> getProductDetailsByProductId(Integer productId);
}