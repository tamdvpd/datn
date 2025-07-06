package com.fashionstore.fashionstore.repository;

import com.fashionstore.fashionstore.entity.ImportInvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImportInvoiceDetailRepository extends JpaRepository<ImportInvoiceDetail, Integer> {

    // Thêm dòng này để fix lỗi
    List<ImportInvoiceDetail> findByImportInvoiceId(Integer importInvoiceId);
}
