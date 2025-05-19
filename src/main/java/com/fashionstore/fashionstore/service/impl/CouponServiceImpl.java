package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.Coupon;
import com.fashionstore.fashionstore.repository.CouponRepository;
import com.fashionstore.fashionstore.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public Page<Coupon> getAllCoupons(Pageable pageable) {
        return couponRepository.findAll(pageable);
    }

    @Override
    public Optional<Coupon> getCouponById(Integer id) {
        return couponRepository.findById(id);
    }

    @Override
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public Coupon updateCoupon(Integer id, Coupon coupon) {
        coupon.setId(id);
        return couponRepository.save(coupon);
    }

    @Override
    public void deleteCoupon(Integer id) {
        couponRepository.deleteById(id);
    }
}