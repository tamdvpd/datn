package com.fashionstore.fashionstore.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public List<Product> getAllProducts() {
        String domain = "http://localhost:8080";
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            // ✅ Cập nhật đường dẫn ảnh sản phẩm
            String image = product.getImageUrl();
            if (image != null && !image.startsWith("/images/products/")) {
                product.setImageUrl(domain + "/images/products/" + image);
            }

            // ✅ Lấy các chi tiết sản phẩm của product
            List<ProductDetail> details = productDetailRepository.findByProductId(product.getId());
            if (!details.isEmpty()) {
                ProductDetail bestDetail = details.get(0);

                for (ProductDetail detail : details) {
                    // Ưu tiên theo discountPrice nếu có
                    if (detail.getDiscountPrice() != null && bestDetail.getDiscountPrice() != null) {
                        if (detail.getDiscountPrice().compareTo(bestDetail.getDiscountPrice()) < 0) {
                            bestDetail = detail;
                        }
                    } else if (detail.getDiscountPrice() != null) {
                        bestDetail = detail;
                    } else if (bestDetail.getDiscountPrice() == null &&
                            detail.getPrice().compareTo(bestDetail.getPrice()) < 0) {
                        bestDetail = detail;
                    }
                }

                // ✅ Gán giá hiển thị vào product (transient)
                product.setDisplayPrice(bestDetail.getPrice());
                product.setDisplayDiscountPrice(bestDetail.getDiscountPrice());
            }
        }

        return products;
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Optional<Product> getProductById(Integer id) {
        Optional<Product> productOpt = productRepository.findById(id);
        String domain = "http://localhost:8080";
        productOpt.ifPresent(product -> {
            // Ép load productDetails
            List<ProductDetail> details = product.getProductDetails();
            for (ProductDetail detail : details) {
                String image = detail.getImageUrl();
                if (image != null && !image.startsWith("http")) {
                    detail.setImageUrl(domain + "/images/productDetails/" + image);
                }
            }

            // Xử lý imageUrl của sản phẩm chính
            String image = product.getImageUrl();
            if (image != null && !image.startsWith("http")) {
                product.setImageUrl(domain + "/images/products/" + image);
            }
        });
        return productOpt;
    }

    @Override
    public Product createProduct(Product product, MultipartFile imageFile) {
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        Product saveProduct = productRepository.save(product);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = storeImageWithId(saveProduct.getId(), imageFile);
            saveProduct.setImageUrl(imageUrl);
            saveProduct = productRepository.save(saveProduct);
        }

        return saveProduct;
    }

    @Override
    public Product updateProduct(Integer id, Product updatedProduct, MultipartFile imageFile) {
        updatedProduct.setUpdatedAt(LocalDateTime.now());

        // Gán lại Category theo ID
        Category category = new Category();
        category.setId(updatedProduct.getCategory().getId());
        updatedProduct.setCategory(category);

        updatedProduct.setId(id); // đảm bảo ID đúng
        Product saveProduct = productRepository.save(updatedProduct);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = storeImageWithId(saveProduct.getId(), imageFile);
            saveProduct.setImageUrl(imageUrl);
            saveProduct = productRepository.save(saveProduct);
        }

        return saveProduct;
    }

    @Override
    public Product updateProduct(Integer id, String name, String description, String brand, Boolean status,
            Integer categoryId, MultipartFile image) {

        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        Product product = optionalProduct.get();
        product.setName(name);
        product.setDescription(description);
        product.setBrand(brand);
        product.setStatus(status);
        product.setUpdatedAt(LocalDateTime.now());

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        product.setCategory(category);

        if (image != null && !image.isEmpty()) {
            String imageUrl = storeImageWithId(product.getId(), image);
            product.setImageUrl(imageUrl);
        }

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDetail> searchProducts(String name, Integer categoryId, BigDecimal minPrice,
            BigDecimal maxPrice) {
        return productDetailRepository.searchProducts(name, categoryId, minPrice, maxPrice);
    }

    // ✅ Lưu ảnh theo ID sản phẩm
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