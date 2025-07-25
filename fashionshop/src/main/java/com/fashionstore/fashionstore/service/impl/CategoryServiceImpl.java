package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.Category;
import com.fashionstore.fashionstore.repository.CategoryRepository;
import com.fashionstore.fashionstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final String uploadDir = "uploads/categories/"; // Thư mục lưu ảnh

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
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = saveImage(imageFile);
            category.setImageUrl(imageUrl);
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = saveImage(imageFile);
            category.setImageUrl(imageUrl);
        }
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }

    /**
     * Lưu ảnh vào thư mục uploads/categories
     */
    private String saveImage(MultipartFile imageFile) {
        try {
            // Tạo thư mục nếu chưa có
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Tạo tên file duy nhất
            String fileExtension = getFileExtension(imageFile.getOriginalFilename());
            String fileName = UUID.randomUUID() + fileExtension;

            Path path = Paths.get(uploadDir + fileName);
            Files.write(path, imageFile.getBytes());

            return "/images/categories/" + fileName; // Đường dẫn trả về (tuỳ frontend)
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu ảnh: " + e.getMessage());
        }
    }

    /**
     * Lấy phần mở rộng của file (vd: .jpg, .png)
     */
    private String getFileExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return "";
    }
}
