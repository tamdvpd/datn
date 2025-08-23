package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.ImportInvoiceDetail;
import java.util.List;

public interface ImportInvoiceDetailService {
    List<ImportInvoiceDetail> getAll();

    List<ImportInvoiceDetail> getByImportInvoiceId(Integer importInvoiceId);

    ImportInvoiceDetail getById(Integer id);

    ImportInvoiceDetail create(ImportInvoiceDetail detail);

    ImportInvoiceDetail update(Integer id, ImportInvoiceDetail detail);

    void delete(Integer id);

    Double getTotalAmountByInvoice(Integer importInvoiceId);}

