package com.fashionstore.fashionstore.repository;

<<<<<<< Updated upstream
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fashionstore.fashionstore.entity.Order;
=======
import org.springframework.data.jpa.repository.JpaRepository;

>>>>>>> Stashed changes
import com.fashionstore.fashionstore.entity.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {

<<<<<<< Updated upstream
    Optional<Order> findById(Long paymentMethodId);

=======
>>>>>>> Stashed changes
}
