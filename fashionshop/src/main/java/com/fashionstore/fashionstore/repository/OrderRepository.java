package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    // 1. USER - Lấy tất cả đơn hàng theo email người dùng
    List<Order> findByUser_Email(String email);

    // 2. USER - Lấy đơn hàng theo email + trạng thái
    List<Order> findByUser_EmailAndStatus(String email, String status);

    // 3. USER - Lấy đơn hàng theo userId (dùng khi không sử dụng email)
    List<Order> findByUser_Id(Integer userId);

    // 4. USER - Lấy 5 đơn gần nhất theo email người dùng
    List<Order> findTop5ByUser_EmailOrderByCreatedAtDesc(String email);

    // 5. ADMIN - Lọc đơn hàng theo trạng thái có phân trang
    Page<Order> findByStatus(String status, Pageable pageable);

    // 6. ADMIN - Lấy danh sách đơn hàng theo trạng thái (không phân trang)
    List<Order> findByStatus(String status);

    // 7. Kiểm tra tồn tại đơn hàng theo ID
    boolean existsById(Integer id);
}
