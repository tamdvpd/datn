package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.ImportInvoice;
import com.fashionstore.fashionstore.repository.ImportInvoiceRepository;
import com.fashionstore.fashionstore.service.ImportInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImportInvoiceServiceImpl implements ImportInvoiceService {

    private final ImportInvoiceRepository importInvoiceRepository;

    @Override
    public List<ImportInvoice> getAllImportInvoices() {
        return importInvoiceRepository.findAll();
    }

    @Override
    public Optional<ImportInvoice> getImportInvoiceById(Integer id) {
        return importInvoiceRepository.findById(id);
    }

    @Override
    public ImportInvoice createImportInvoice(ImportInvoice invoice) {
        return importInvoiceRepository.save(invoice);
    }

    @Override
    public ImportInvoice updateImportInvoice(Integer id, ImportInvoice invoice) {
        return importInvoiceRepository.save(invoice);
    }

    @Override
    public void deleteImportInvoice(Integer id) {
        importInvoiceRepository.deleteById(id);
    }
}
