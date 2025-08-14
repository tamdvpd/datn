package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CouponService {
    List<Coupon> getAllCoupons();

    Page<Coupon> getAllCoupons(Pageable pageable);

    Optional<Coupon> getCouponById(Integer id);

    Coupon createCoupon(Coupon coupon);

    Coupon updateCoupon(Integer id, Coupon coupon);

    Coupon updateQuantity(Integer id);

    void deleteCoupon(Integer id);

    boolean checkByCode(String code);

    Optional<Coupon> getCouponByCode(String code);
}