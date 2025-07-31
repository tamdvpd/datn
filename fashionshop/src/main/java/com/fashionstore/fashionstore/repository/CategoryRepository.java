package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // Kiểm tra danh mục có tồn tại theo tên (dùng để tránh trùng khi thêm mới)
    boolean existsByName(String name);

    // Tìm danh mục theo tên (không phân biệt hoa thường)
    Optional<Category> findByNameIgnoreCase(String name);
}
