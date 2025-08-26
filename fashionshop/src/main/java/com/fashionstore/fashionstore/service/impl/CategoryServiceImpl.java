package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.Category;
import com.fashionstore.fashionstore.repository.CategoryRepository;
import com.fashionstore.fashionstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category createCategory(Category category, MultipartFile imageFile) {
        // Bước 1: Lưu tạm để sinh ID
        Category savedCategory = categoryRepository.save(category);

        // Bước 2: Nếu có ảnh, lưu ảnh theo ID
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = storeImageWithId(savedCategory.getId(), imageFile);
            savedCategory.setImageUrl(imageUrl);

            // Bước 3: Lưu lại để cập nhật imageUrl
            savedCategory = categoryRepository.save(savedCategory);
        }

        return savedCategory;
    }

    @Override
    public Category updateCategory(Category category, MultipartFile imageFile) {
        // Bước 1: Lưu tạm để sinh ID
        Category savedCategory = categoryRepository.save(category);

        // Bước 2: Nếu có ảnh, lưu ảnh theo ID
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = storeImageWithId(savedCategory.getId(), imageFile);
            savedCategory.setImageUrl(imageUrl);

            // Bước 3: Lưu lại để cập nhật imageUrl
            savedCategory = categoryRepository.save(savedCategory);
        }

        return savedCategory;
    }

    @Override
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
    
    @Override
    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }
    
    private String storeImageWithId(Integer categoryId, MultipartFile file) {
        try {
            // Đường dẫn thư mục ảnh trong thư mục gốc dự án
            String baseDir = System.getProperty("user.dir"); // thư mục project
            String uploadDir = baseDir + "/upload/images/categories";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Lấy phần mở rộng file
            String originalFileName = file.getOriginalFilename();
            String extension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }

            // Đặt tên file theo ID
            String fileName = categoryId + extension;
            Path filePath = uploadPath.resolve(fileName);

            file.transferTo(filePath.toFile());

            // Trả về đường dẫn dùng để hiển thị trên frontend
            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("Không thể lưu ảnh: " + e.getMessage(), e);
        }
    }
}
