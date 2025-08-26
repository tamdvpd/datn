package com.fashionstore.fashionstore.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fashionstore.fashionstore.entity.Category;
import com.fashionstore.fashionstore.entity.Product;
import com.fashionstore.fashionstore.entity.ProductDetail;
import com.fashionstore.fashionstore.repository.CategoryRepository;
import com.fashionstore.fashionstore.repository.ProductDetailRepository;
import com.fashionstore.fashionstore.repository.ProductRepository;
import com.fashionstore.fashionstore.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductDetailRepository productDetailRepository;

    private final String DOMAIN = "http://localhost:8080";

    // ✅ Lấy tất cả sản phẩm
    @Override
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        products.forEach(this::setProductDisplayInfo);
        return products;
    }

    // ✅ Lấy sản phẩm phân trang
    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        products.forEach(this::setProductDisplayInfo);
        return products;
    }

    // ✅ Lấy sản phẩm theo ID
    @Override
    public Optional<Product> getProductById(Integer id) {
        Optional<Product> productOpt = productRepository.findById(id);
        productOpt.ifPresent(this::setProductDisplayInfo);
        return productOpt;
    }

    // ✅ Tạo sản phẩm
    @Override
    public Product createProduct(Product product, MultipartFile imageFile) {
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        Product saved = productRepository.save(product);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = storeImageWithId(saved.getId(), imageFile);
            saved.setImageUrl(imageUrl);
            saved = productRepository.save(saved);
        }
        return saved;
    }

    // ✅ Cập nhật sản phẩm (truyền object)
    @Override
    public Product updateProduct(Integer id, Product updatedProduct, MultipartFile imageFile) {
        updatedProduct.setId(id);
        updatedProduct.setUpdatedAt(LocalDateTime.now());

        if (updatedProduct.getCategory() != null) {
            Category category = categoryRepository.findById(updatedProduct.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            updatedProduct.setCategory(category);
        }

        Product saved = productRepository.save(updatedProduct);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = storeImageWithId(saved.getId(), imageFile);
            saved.setImageUrl(imageUrl);
            saved = productRepository.save(saved);
        }
        return saved;
    }

    // ✅ Cập nhật sản phẩm (trường lẻ)
    @Override
    public Product updateProduct(Integer id, String name, String description, String brand,
            Boolean status, Integer categoryId, MultipartFile image) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(name);
        product.setDescription(description);
        product.setBrand(brand);
        product.setStatus(status);
        product.setUpdatedAt(LocalDateTime.now());

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        if (image != null && !image.isEmpty()) {
            String imageUrl = storeImageWithId(product.getId(), image);
            product.setImageUrl(imageUrl);
        }
        return productRepository.save(product);
    }

    // ✅ Xoá sản phẩm
    @Override
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<Product> searchProducts(String keyword, Integer categoryId,
            BigDecimal minPrice, BigDecimal maxPrice,
            String sortBy, String order,
            int page, int size) {

        // === Chọn sort theo sortBy ===
        Sort sort;
        switch (sortBy != null ? sortBy : "") {
            case "az":
                sort = Sort.by("name").ascending();
                break;
            case "za":
                sort = Sort.by("name").descending();
                break;
            case "priceAsc":
                sort = Sort.by("displayPrice").ascending();
                break;
            case "priceDesc":
                sort = Sort.by("displayPrice").descending();
                break;
            case "bestSeller":
                sort = Sort.by("totalSold").descending();
                break;
            case "newest":
                sort = Sort.by("createdAt").descending();
                break;
            default:
                sort = Sort.by("id").ascending();
        }

        // === Override order nếu có ===
        if (order != null) {
            if ("asc".equalsIgnoreCase(order))
                sort = sort.ascending();
            if ("desc".equalsIgnoreCase(order))
                sort = sort.descending();
        }

        // === Tạo Pageable với sort ===
        Pageable pageable = PageRequest.of(page, size, sort);

        // === Gọi repository để tìm kiếm ===
        Page<Product> products = productRepository.searchProducts(
                keyword, categoryId, minPrice, maxPrice, pageable);

        // === Thiết lập hiển thị giá và ảnh cho từng sản phẩm ===
        products.forEach(this::setProductDisplayInfo);

        return products;
    }

    // ✅ Tìm kiếm sản phẩm không phân trang (dùng trong admin/kho)
    @Override
    public List<ProductDetail> searchProducts(String name, Integer categoryId, BigDecimal minPrice,
            BigDecimal maxPrice) {
        return productDetailRepository.searchProducts(name, categoryId, minPrice, maxPrice);
    }

    // ✅ Lấy sản phẩm bán chạy
    @Override
    public List<Product> getBestSellers(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("totalSold").descending());
        Page<Product> products = productRepository.findAll(pageable);
        products.forEach(this::setProductDisplayInfo);
        return products.getContent();
    }

    // === Private methods ===

    // Set ảnh & giá hiển thị
    private void setProductDisplayInfo(Product product) {
        if (product.getImageUrl() != null && !product.getImageUrl().startsWith("http")) {
            product.setImageUrl(DOMAIN + "/images/products/" + product.getImageUrl());
        }

        List<ProductDetail> details = productDetailRepository.findByProductId(product.getId());
        if (!details.isEmpty()) {
            ProductDetail bestDetail = details.stream()
                    .min(Comparator.comparing(detail -> {
                        if (detail.getDiscountPrice() != null)
                            return detail.getDiscountPrice();
                        return detail.getPrice();
                    }))
                    .orElse(details.get(0));

            product.setDisplayPrice(bestDetail.getPrice());
            product.setDisplayDiscountPrice(bestDetail.getDiscountPrice());
        }
    }

    // Lưu ảnh
    private String storeImageWithId(Integer productId, MultipartFile file) {
        try {
            String baseDir = System.getProperty("user.dir");
            String uploadDir = baseDir + "/upload/images/products/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFileName = file.getOriginalFilename();
            String extension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }

            String fileName = productId + extension;
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Không thể lưu ảnh: " + e.getMessage(), e);
        }
    }
}