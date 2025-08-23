package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.ImportInvoiceDetail;
import com.fashionstore.fashionstore.repository.ImportInvoiceDetailRepository;
import com.fashionstore.fashionstore.service.ImportInvoiceDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
                .orElseThrow(() -> new RuntimeException("ImportInvoiceDetail not found with id " + id));
    }

    @Override
    public ImportInvoiceDetail create(ImportInvoiceDetail detail) {
        return importInvoiceDetailRepository.save(detail);
    }

    @Override
    public ImportInvoiceDetail update(Integer id, ImportInvoiceDetail detail) {
        ImportInvoiceDetail existing = importInvoiceDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detail not found"));

        existing.setQuantity(detail.getQuantity());
        existing.setUnitPrice(detail.getUnitPrice());
        existing.setProductDetail(detail.getProductDetail());
        existing.setUser(detail.getUser());

        return importInvoiceDetailRepository.save(existing);
    }

    @Override
    public void delete(Integer id) {
        if (!importInvoiceDetailRepository.existsById(id)) {
            throw new RuntimeException("Detail not found with id " + id);
        }
        importInvoiceDetailRepository.deleteById(id);
    }

    @Override
    public Double getTotalAmountByInvoice(Integer importInvoiceId) {
        List<ImportInvoiceDetail> details = importInvoiceDetailRepository.findByImportInvoiceId(importInvoiceId);

        return details.stream()
                .map(d -> d.getUnitPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doubleValue();
    }
}

