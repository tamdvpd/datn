package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

        @EntityGraph(attributePaths = { "user", "orderDetails" })
        List<Order> findByUser_Email(String email);

        @EntityGraph(attributePaths = { "user", "orderDetails" })
        List<Order> findByUser_EmailAndStatus(String email, String status);

        @EntityGraph(attributePaths = { "user", "orderDetails" })
        List<Order> findTop5ByUser_EmailOrderByCreatedAtDesc(String email);

        @EntityGraph(attributePaths = { "user", "orderDetails" })
        List<Order> findByUser_Id(Integer userId);

        @EntityGraph(attributePaths = { "user", "orderDetails" })
        List<Order> findByUser_IdAndStatus(Integer userId, String status);

        @EntityGraph(attributePaths = { "user", "orderDetails" })
        Page<Order> findByUser_Id(Integer userId, Pageable pageable);

        @EntityGraph(attributePaths = { "user", "orderDetails" })
        Page<Order> findByUser_IdAndStatus(Integer userId, String status, Pageable pageable);

        @EntityGraph(attributePaths = { "user", "orderDetails" })
        Page<Order> findByStatus(String status, Pageable pageable);

        @EntityGraph(attributePaths = { "user", "orderDetails" })
        List<Order> findByStatus(String status);

        @EntityGraph(attributePaths = {
                        "user",
                        "orderDetails",
                        "orderDetails.productDetail",
                        "orderDetails.productDetail.product"
        })

        boolean existsById(Integer id);

        Optional<Order> findOneWithDetailsById(Integer id);

        // doanh thu hôm nay
        @Query(value = """
                        SELECT SUM(total_amount)
                        FROM Orders
                        WHERE CAST(created_at AS DATE) = CAST(GETDATE() AS DATE)
                          AND status = 'COMPLETED'
                        """, nativeQuery = true)
        long getRevenueToday();

        // doanh thu tuần hiện tại
        @Query(value = """
                        SELECT SUM(total_amount)
                        FROM Orders
                        WHERE DATEPART(ISO_WEEK, created_at) = DATEPART(ISO_WEEK, GETDATE())
                          AND YEAR(created_at) = YEAR(GETDATE())
                          AND status = 'COMPLETED'
                        """, nativeQuery = true)
        long getRevenueThisWeek();

        // doanh thu tháng hiện tại
        @Query(value = """
                        SELECT SUM(total_amount)
                        FROM Orders
                        WHERE MONTH(created_at) = MONTH(GETDATE())
                          AND YEAR(created_at) = YEAR(GETDATE())
                          AND status = 'COMPLETED'
                        """, nativeQuery = true)
        long getRevenueThisMonth();

        // doanh thu năm hiện tại
        @Query(value = """
                        SELECT SUM(total_amount)
                        FROM Orders
                        WHERE YEAR(created_at) = YEAR(GETDATE())
                          AND status = 'COMPLETED'
                        """, nativeQuery = true)
        long getRevenueThisYear();

        // tổng số đơn hàng bị hủy
        @Query(value = """
                        SELECT COUNT(*)
                        FROM Orders
                        WHERE status = 'CANCELLED'
                        """, nativeQuery = true)
        long countCancelledOrders();

        // tổng số đơn hàng hoàn thành
        @Query(value = """
                        SELECT COUNT(*)
                        FROM Orders
                        WHERE status = 'COMPLETED'
                        """, nativeQuery = true)
        long countCompletedOrders();

        // tổng số đơn hàng chờ xác nhận
        @Query(value = """
                        SELECT COUNT(*)
                        FROM Orders
                        WHERE status = 'Pending Confirmation'
                        """, nativeQuery = true)
        long countPendingOrders();

        // tổng số đơn hàng đang giao
        @Query(value = """
                        SELECT COUNT(*)
                        FROM Orders
                        WHERE status = 'SHIPPED'
                        """, nativeQuery = true)
        long countSHIPPEDOrders();
}