package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.ImportInvoice;
import com.fashionstore.fashionstore.entity.ImportInvoiceDetail;

import java.util.List;
import java.util.Optional;

public interface ImportInvoiceService {

    // Phiếu nhập
    List<ImportInvoice> getAllImportInvoices();

    Optional<ImportInvoice> getImportInvoiceById(Integer id);

    ImportInvoice createImportInvoice(ImportInvoice invoice);

    ImportInvoice updateImportInvoice(Integer id, ImportInvoice invoice);

    void deleteImportInvoice(Integer id);

    // Chi tiết phiếu nhập
    ImportInvoiceDetail createInvoiceDetail(Integer invoiceId, ImportInvoiceDetail detail);

    ImportInvoiceDetail updateInvoiceDetail(Integer detailId, ImportInvoiceDetail detail);

    void deleteInvoiceDetail(Integer detailId);

    List<ImportInvoiceDetail> getInvoiceDetailsByInvoiceId(Integer invoiceId);

    // Nhập kho
    void importStock(Integer invoiceId);
}
