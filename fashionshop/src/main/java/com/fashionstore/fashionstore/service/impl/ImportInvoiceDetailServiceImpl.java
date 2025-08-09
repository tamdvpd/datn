package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.ImportInvoiceDetail;
import com.fashionstore.fashionstore.repository.ImportInvoiceDetailRepository;
import com.fashionstore.fashionstore.service.ImportInvoiceDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportInvoiceDetailServiceImpl implements ImportInvoiceDetailService {

    private final ImportInvoiceDetailRepository importInvoiceDetailRepository;

    @Override
    public List<ImportInvoiceDetail> getAll() {
        return importInvoiceDetailRepository.findAll();
    }

    @Override
    public List<ImportInvoiceDetail> getByImportInvoiceId(Integer importInvoiceId) {
        return importInvoiceDetailRepository.findByImportInvoiceId(importInvoiceId);
    }

    @Override
    public ImportInvoiceDetail getById(Integer id) {
        return importInvoiceDetailRepository.findById(id)
                .orElse(null);
    }

    @Override
    public ImportInvoiceDetail create(ImportInvoiceDetail detail) {
        return importInvoiceDetailRepository.save(detail);
    }

    @Override
    public void delete(Integer id) {
        importInvoiceDetailRepository.deleteById(id);
    }
}
