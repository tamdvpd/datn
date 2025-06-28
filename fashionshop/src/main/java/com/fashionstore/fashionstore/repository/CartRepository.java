package com.fashionstore.fashionstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fashionstore.fashionstore.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {

}
