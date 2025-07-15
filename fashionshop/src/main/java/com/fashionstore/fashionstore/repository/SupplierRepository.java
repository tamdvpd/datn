package com.fashionstore.fashionstore.repository;

<<<<<<< Updated upstream
import com.fashionstore.fashionstore.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    // Cho phép trả về nhiều kết quả để tránh lỗi
    List<Supplier> findByEmail(String email);

    List<Supplier> findByPhoneNumber(String phoneNumber);
    
    List<Supplier> findByStatusTrue();
}
=======
import org.springframework.data.jpa.repository.JpaRepository;

import com.fashionstore.fashionstore.entity.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

}
>>>>>>> Stashed changes
