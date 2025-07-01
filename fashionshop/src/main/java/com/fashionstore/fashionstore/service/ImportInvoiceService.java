package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.ImportInvoice;

import java.util.List;
import java.util.Optional;

public interface ImportInvoiceService {
    List<ImportInvoice> getAllImportInvoices();

    Optional<ImportInvoice> getImportInvoiceById(Integer id);

    ImportInvoice createImportInvoice(ImportInvoice invoice);

    ImportInvoice updateImportInvoice(Integer id, ImportInvoice invoice);

    void deleteImportInvoice(Integer id);
}
