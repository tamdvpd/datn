package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.Coupon;
import com.fashionstore.fashionstore.service.CouponService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3001/")
@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @GetMapping
    public ResponseEntity<Page<Coupon>> getAll(@RequestParam(defaultValue = "0") int page) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Coupon> result = couponService.getAllCoupons(pageable);
        return ResponseEntity.ok(result);
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

    @GetMapping("/check-code/{code}")
    public ResponseEntity<Coupon> checkCoupon(@PathVariable String code) {
        boolean result = couponService.checkByCode(code);
        if (result) {
            return couponService.getCouponByCode(code)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}