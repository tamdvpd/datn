package com.fashionstore.fashionstore.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fashionstore.fashionstore.entity.ShippingProvider;
import com.fashionstore.fashionstore.service.ShippingProviderService;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3001/")
@RestController
@RequestMapping("/shipping-providers")
@RequiredArgsConstructor

public class ShippingProviderController {

    private final ShippingProviderService shippingProviderService;

    @GetMapping
    public ResponseEntity<List<ShippingProvider>> getAll() {
        return ResponseEntity.ok(shippingProviderService.getAllShipping());
    }

    @GetMapping("/active")
    public ResponseEntity<List<ShippingProvider>> getAllActive() {
        return ResponseEntity.ok(shippingProviderService.getAllShippingIsActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShippingProvider> getById(@PathVariable Integer id) {
        return ResponseEntity.of(shippingProviderService.getById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid ShippingProvider provider, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }

        return ResponseEntity.ok(shippingProviderService.create(provider));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @RequestBody @Valid ShippingProvider provider,
            BindingResult result) {

        if (result.hasErrors()) {
            System.out.println("❌ Validation lỗi nè!"); // ← Dòng để test log
            List<String> errors = result.getFieldErrors().stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        return ResponseEntity.ok(shippingProviderService.update(id, provider));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        shippingProviderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
