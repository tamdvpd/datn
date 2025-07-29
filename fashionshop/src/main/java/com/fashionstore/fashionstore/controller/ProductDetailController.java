package com.fashionstore.fashionstore.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fashionstore.fashionstore.entity.Product;
import com.fashionstore.fashionstore.entity.ProductDetail;
import com.fashionstore.fashionstore.service.ProductDetailService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/productdetails")
@RequiredArgsConstructor
public class ProductDetailController {

    private final ProductDetailService productDetailService;

    @GetMapping
    public ResponseEntity<List<ProductDetail>> getAllProductDetails() {
        return ResponseEntity.ok(productDetailService.getAllProductDetails());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetail> getProductDetailById(@PathVariable Integer id) {
        return productDetailService.getProductDetailById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductDetail>> getProductDetailsByProductId(@PathVariable Integer productId) {
        return ResponseEntity.ok(productDetailService.getProductDetailsByProductId(productId));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProductDetail(
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @RequestParam("color") String color,
            @RequestParam("size") String size,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("price") BigDecimal price,
            @RequestParam("discountPrice") BigDecimal discountPrice,
            @RequestParam("weight") BigDecimal weight,
            @RequestParam("productId") Integer productId) {
        try {
            ProductDetail productDetailBuilder = ProductDetail.builder()
                    .color(color)
                    .size(size)
                    .quantity(quantity)
                    .price(price)
                    .discountPrice(discountPrice)
                    .weight(weight)
                    .build();
            Product product = new Product();
            product.setId(productId);
            productDetailBuilder.setProduct(product);
            ProductDetail createdProductDetail = productDetailService.createProductDetail(productDetailBuilder,
                    imageFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProductDetail);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating product detail");
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProductDetail(
            @PathVariable Integer id,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @RequestParam("color") String color,
            @RequestParam("size") String size,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("price") BigDecimal price,
            @RequestParam("discountPrice") BigDecimal discountPrice,
            @RequestParam("weight") BigDecimal weight,
            @RequestParam("productId") Integer productId) {
        try {
            ProductDetail productDetail = ProductDetail.builder()
                    .id(id)
                    .color(color)
                    .size(size)
                    .quantity(quantity)
                    .price(price)
                    .discountPrice(discountPrice)
                    .weight(weight)
                    .build();

            Product product = new Product();
            product.setId(productId);
            productDetail.setProduct(product);

            // ✅ Gọi đúng hàm update
            ProductDetail updated = productDetailService.updateProductDetail(id, productDetail, imageFile);

            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi cập nhật chi tiết sản phẩm với id: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductDetail(@PathVariable Integer id) {
        productDetailService.deleteProductDetail(id);
        return ResponseEntity.noContent().build();
    }
}