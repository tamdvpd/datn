package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.ProductDetail;
import com.fashionstore.fashionstore.repository.ProductDetailRepository;
import com.fashionstore.fashionstore.service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/productDetails")
@RequiredArgsConstructor
public class ProductDetailController {

    private final ProductDetailService productDetailService;
    private final ProductDetailRepository productDetailRepository;

    @GetMapping
    public ResponseEntity<List<ProductDetail>> getAll() {
        return ResponseEntity.ok(productDetailService.getAllProductDetails());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetail> getById(@PathVariable Integer id) {
        return ResponseEntity.of(productDetailService.getProductDetailById(id));
    }

    @GetMapping("/by-product/{productId}")
    public ResponseEntity<ProductDetail> getByProductId(@PathVariable Integer productId) {
        return ResponseEntity.of(productDetailService.getByProductId(productId));
    }

    @PostMapping
    public ResponseEntity<ProductDetail> create(@RequestBody ProductDetail productDetail) {
        return ResponseEntity.ok(productDetailService.createProductDetail(productDetail));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDetail> update(@PathVariable Integer id, @RequestBody ProductDetail productDetail) {
        return ResponseEntity.ok(productDetailService.updateProductDetail(id, productDetail));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productDetailService.deleteProductDetail(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/test-price/{productId}")
public BigDecimal testGetPrice(@PathVariable Integer productId) {
    return productDetailRepository
             .findFirstByProduct_Id(productId)
             .map(ProductDetail::getPrice)
             .orElse(BigDecimal.ZERO);
}

}
