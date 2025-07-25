package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.Order;
import com.fashionstore.fashionstore.entity.OrderStatus;
import com.fashionstore.fashionstore.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {

    // 🔹 1. Lấy đơn hàng theo trạng thái (enum/string)
    Page<Order> findByStatus(String status, Pageable pageable);
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    // 🔹 2. Lấy đơn hàng theo người dùng
    List<Order> findByUser(User user);
    Page<Order> findByUser(User user, Pageable pageable);

    // 🔹 3. Theo người dùng + trạng thái
    List<Order> findByUserAndStatus(User user, String status);
    List<Order> findByUserAndStatus(User user, OrderStatus status);

    // 🔹 4. Theo userId
    List<Order> findByUserId(Integer userId);
    Page<Order> findByUserId(Integer userId, Pageable pageable);

    // 🔹 5. Theo userId + trạng thái
    List<Order> findByUserIdAndStatus(Integer userId, String status);
    List<Order> findByUserIdAndStatus(Integer userId, OrderStatus status);
    Page<Order> findByUserIdAndStatus(Integer userId, String status, Pageable pageable);
    Page<Order> findByUserIdAndStatus(Integer userId, OrderStatus status, Pageable pageable);

    // 🔹 6. Tìm theo khoảng thời gian (dashboard/thống kê)
    @Query("""
        SELECT o FROM Order o
        WHERE o.createdAt BETWEEN :start AND :end
    """)
    List<Order> findAllByCreatedAtBetween(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    // 🔹 7. Kiểm tra trùng mã vận đơn
    boolean existsByTrackingCode(String trackingCode);

    // 🔹 8. Đếm số lượng đơn theo trạng thái (dashboard)
    long countByStatus(String status);
    long countByStatus(OrderStatus status);

    // 🔹 9. Top N đơn mới nhất
    List<Order> findTop10ByOrderByCreatedAtDesc();

    // 🔹 10. Tìm theo trạng thái + thời gian (tuỳ chọn)
    @Query("""
        SELECT o FROM Order o
        WHERE (:status IS NULL OR o.status = :status)
          AND o.createdAt BETWEEN :from AND :to
    """)
    Page<Order> searchByStatusAndCreatedAtBetween(
        @Param("status") String status,
        @Param("from") LocalDateTime from,
        @Param("to") LocalDateTime to,
        Pageable pageable
    );

    @Query("""
        SELECT o FROM Order o
        WHERE (:status IS NULL OR o.status = :status)
          AND o.createdAt BETWEEN :from AND :to
    """)
    Page<Order> searchByStatusAndCreatedAtBetween(
        @Param("status") OrderStatus status,
        @Param("from") LocalDateTime from,
        @Param("to") LocalDateTime to,
        Pageable pageable
    );

    // 🔹 11. Thống kê tổng doanh thu (chỉ đơn hoàn tất)
    @Query("""
        SELECT COALESCE(SUM(o.totalAmount - o.discountAmount + o.shippingFee), 0)
        FROM Order o
        WHERE o.status = :status
          AND o.createdAt BETWEEN :from AND :to
    """)
    BigDecimal totalRevenueBetween(
        @Param("status") OrderStatus status,
        @Param("from") LocalDateTime from,
        @Param("to") LocalDateTime to
    );

    // 🔹 12. Đếm đơn hàng theo khoảng thời gian + trạng thái
    @Query("""
        SELECT COUNT(o) FROM Order o
        WHERE o.status = :status
          AND o.createdAt BETWEEN :from AND :to
    """)
    long countByStatusAndDateBetween(
        @Param("status") OrderStatus status,
        @Param("from") LocalDateTime from,
        @Param("to") LocalDateTime to
    );

    // 🔹 13. Tổng số lượng đơn của 1 user (thống kê user)
    long countByUserId(Integer userId);

    // 🔹 14. Tổng đơn theo nhiều trạng thái
    Page<Order> findByStatusIn(List<OrderStatus> statuses, Pageable pageable);
}
