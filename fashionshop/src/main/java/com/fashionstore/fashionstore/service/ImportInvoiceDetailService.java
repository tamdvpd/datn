package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.ImportInvoiceDetail;

import java.util.List;

public interface ImportInvoiceDetailService {

    // Lấy tất cả dòng chi tiết phiếu nhập
    List<ImportInvoiceDetail> findAll();

    // Thêm mới hoặc cập nhật 1 dòng chi tiết
    ImportInvoiceDetail save(ImportInvoiceDetail detail);

    //  Lấy danh sách dòng chi tiết theo mã phiếu nhập
    List<ImportInvoiceDetail> getByImportInvoiceId(Integer importInvoiceId);

    // Xóa 1 dòng chi tiết theo ID
    void deleteById(Integer id);
}
