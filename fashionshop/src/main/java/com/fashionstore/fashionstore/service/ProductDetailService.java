package com.fashionstore.fashionstore.service;

import java.util.Optional;

import com.fashionstore.fashionstore.entity.ProductDetail;

public interface ProductDetailService {
    Optional<ProductDetail> getProductDetailById(Integer id);
}