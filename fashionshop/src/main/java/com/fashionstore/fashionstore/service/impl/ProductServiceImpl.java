package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.Product;
import com.fashionstore.fashionstore.entity.ProductDetail;
import com.fashionstore.fashionstore.entity.Review;
import com.fashionstore.fashionstore.repository.ProductDetailRepository;
import com.fashionstore.fashionstore.repository.ProductRepository;
import com.fashionstore.fashionstore.repository.ReviewRepository;
import com.fashionstore.fashionstore.service.ProductService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import com.fashionstore.fashionstore.dto.ProductSimpleDTO;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductDetailRepository detailRepo;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Integer id, Product product) {
        product.setId(id);
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> searchProducts(String name, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        return ProductDetailRepository.searchProducts(name, categoryId, minPrice, maxPrice);
    }

    
    @Override
    public ProductDetailRepository getProductDetail(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        List<ProductDetail> variants = detailRepo.findByProductId(productId);

        if (variants.isEmpty()) {
            throw new EntityNotFoundException("No product details found for product id: " + productId);
        }

        return new ProductDetailRepository() {
            @Override
            public List<ProductDetail> findByProductId(Integer productId) {
                return variants;
            }

            @Override
            public ProductDetail findById(Integer id) {
                return variants.stream()
                        .filter(detail -> detail.getId().equals(id))
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Product detail not found for id: " + id));
            }
        };
    }

    // Add a ReviewRepository field
    private final ReviewRepository reviewRepository;

    @Override
    public List<Review> getReviews(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));   
    }

    @Override
    public List<ProductSimpleDTO> getSuggestedProducts(Integer productId) {
        List<Product> randomProducts = productRepository.findAll(PageRequest.of(0, 4, org.springframework.data.domain.Sort.by("id").descending()))
                .getContent();
        return randomProducts.stream()
                .map(p -> {
                    ProductDetail cheapest = p.getDetails().stream()
                            .min(java.util.Comparator.comparing(ProductDetail::getDiscountPrice))
                            .orElse(null);
                    return ProductSimpleDTO.builder()
                            .id(p.getId())
                            .name(p.getName())
                            .discountPrice(
                                    cheapest != null ? cheapest.getDiscountPrice() : BigDecimal.ZERO)
                            .image(p.getImageUrl())
                            .build();
                }).toList();
    }
}