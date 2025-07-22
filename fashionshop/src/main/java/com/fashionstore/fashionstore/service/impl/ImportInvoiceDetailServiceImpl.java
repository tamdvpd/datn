package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.ImportInvoiceDetail;
import com.fashionstore.fashionstore.repository.ImportInvoiceDetailRepository;
import com.fashionstore.fashionstore.service.ImportInvoiceDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImportInvoiceDetailServiceImpl implements ImportInvoiceDetailService {

    private final ImportInvoiceDetailRepository repository;

    @Override
    public List<ImportInvoiceDetail> getAll() {
        return repository.findAll();
    }

    @Override
    public List<ImportInvoiceDetail> getByImportInvoiceId(Integer importInvoiceId) {
        return repository.findByImportInvoiceId(importInvoiceId);
    }

    @Override
    public ImportInvoiceDetail getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public ImportInvoiceDetail create(ImportInvoiceDetail detail) {
        return repository.save(detail);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
