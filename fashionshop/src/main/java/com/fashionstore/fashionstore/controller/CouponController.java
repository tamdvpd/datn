package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.Coupon;
import com.fashionstore.fashionstore.service.CouponService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3001/")
@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @GetMapping
    public ResponseEntity<List<Coupon>> getAll() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coupon> getById(@PathVariable Integer id) {
        return ResponseEntity.of(couponService.getCouponById(id));
    }

    @PostMapping
    public ResponseEntity<Coupon> create(@Valid @RequestBody Coupon coupon) {
        return ResponseEntity.status(201).body(couponService.createCoupon(coupon));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coupon> update(@PathVariable Integer id, @Valid @RequestBody Coupon coupon) {
        return ResponseEntity.status(201).body(couponService.updateCoupon(id, coupon));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }
}