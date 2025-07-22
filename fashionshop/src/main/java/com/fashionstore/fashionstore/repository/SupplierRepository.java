package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    List<Supplier> findByEmail(String email);

    List<Supplier> findByPhoneNumber(String phoneNumber);

    List<Supplier> findByStatusTrue();

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}
