package com.fashionstore.fashionstore.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fashionstore.fashionstore.entity.Product;
import com.fashionstore.fashionstore.entity.ProductDetail;
import com.fashionstore.fashionstore.repository.ProductDetailRepository;
import com.fashionstore.fashionstore.repository.ProductRepository;
import com.fashionstore.fashionstore.service.ProductDetailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductDetailServiceImpl implements ProductDetailService {

    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository productRepository;

    @Override
    public List<ProductDetail> getAllProductDetails() {
        String domain = "http://localhost:8080";
        List<ProductDetail> productDetails = productDetailRepository.findAll();
        for (ProductDetail detail : productDetails) {
            String image = detail.getImageUrl();
            if (image != null && !image.startsWith("/images/productDetails/")) {
                detail.setImageUrl(domain + "/images/productDetails/" + image);
            }
        }
        return productDetails;
    }

    @Override
    public Optional<ProductDetail> getProductDetailById(Integer id) {
        return productDetailRepository.findById(id);
    }

    @Override
    public ProductDetail createProductDetail(ProductDetail productDetail, MultipartFile imageFile) {
        Optional<Product> productOpt = productRepository.findById(productDetail.getProduct().getId());
        productOpt.ifPresent(productDetail::setProduct);

        // Lưu lần đầu để có ID
        ProductDetail saveProductDetail = productDetailRepository.save(productDetail);

        // Nếu có ảnh thì lưu ảnh và cập nhật lại
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = storeImageWithId(saveProductDetail.getId(), imageFile);
            saveProductDetail.setImageUrl(imageUrl);

            // Lưu lại với imageUrl đã có
            saveProductDetail = productDetailRepository.save(saveProductDetail);
        }

        return saveProductDetail;
    }

    @Override
    public ProductDetail updateProductDetail(Integer id, ProductDetail productDetail, MultipartFile imageFile) {
        ProductDetail existing = productDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết sản phẩm."));

        // Cập nhật các trường thông thường
        existing.setColor(productDetail.getColor());
        existing.setSize(productDetail.getSize());
        existing.setQuantity(productDetail.getQuantity());
        existing.setPrice(productDetail.getPrice());
        existing.setDiscountPrice(productDetail.getDiscountPrice());
        existing.setWeight(productDetail.getWeight());

        // Gán lại product nếu có
        Optional<Product> productOpt = productRepository.findById(productDetail.getProduct().getId());
        productOpt.ifPresent(existing::setProduct);

        // ✅ Nếu có ảnh mới => lưu ảnh & cập nhật đường dẫn
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = storeImageWithId(existing.getId(), imageFile);
            existing.setImageUrl(imageUrl);
        }
        // ❌ Nếu không có ảnh mới => giữ nguyên ảnh cũ => KHÔNG làm gì cả

        return productDetailRepository.save(existing);
    }

    @Override
    public void deleteProductDetail(Integer id) {
        productDetailRepository.deleteById(id);
    }

    @Override
    public List<ProductDetail> getProductDetailsByProductId(Integer productId) {
        String domain = "http://localhost:8080";
        List<ProductDetail> productDetails = productDetailRepository.findByProductId(productId);
        for (ProductDetail detail : productDetails) {
            String image = detail.getImageUrl();
            if (image != null && !image.startsWith("http")) {
                detail.setImageUrl(domain + "/images/productDetails/" + image);
            }
        }
        return productDetails;
    }

    private String storeImageWithId(Integer productDetailId, MultipartFile file) {
        try {
            String baseDir = System.getProperty("user.dir");
            String uploadDir = baseDir + "/upload/images/productDetails/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFileName = file.getOriginalFilename();
            String extension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }

            String fileName = productDetailId + extension;
            Path filePath = uploadPath.resolve(fileName);

            file.transferTo(filePath.toFile());

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Không thể lưu ảnh: " + e.getMessage(), e);
        }
    }
}