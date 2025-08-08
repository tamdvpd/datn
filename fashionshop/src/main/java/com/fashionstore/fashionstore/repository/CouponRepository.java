package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    Coupon findByCode(String code);
}