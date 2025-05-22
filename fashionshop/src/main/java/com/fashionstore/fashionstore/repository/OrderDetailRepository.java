package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
}