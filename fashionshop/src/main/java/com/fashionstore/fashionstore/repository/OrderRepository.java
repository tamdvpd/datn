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
}
