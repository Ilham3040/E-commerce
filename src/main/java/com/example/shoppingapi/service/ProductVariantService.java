package com.example.shoppingapi.service;

import com.example.shoppingapi.repository.ProductVariantRepository;
import com.example.shoppingapi.model.*;
import com.example.shoppingapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductVariantService {

    private final ProductVariantRepository variantRepo;
    private final ProductRepository        productRepo;

    public List<ProductVariant> findAll() {
        return variantRepo.findAll();
    }

    public ProductVariant findById(Long id) {
        return variantRepo.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("ProductVariant not found with ID: " + id));
    }

    public ProductVariant findByProductId(Long productId) {
        return variantRepo.findProductVariantbyProductId(productId)
            .orElseThrow(() ->
                new ResourceNotFoundException("ProductVariant not found for product ID: " + productId));
    }

    public ProductVariant saveProductVariant(ProductVariant variant) {
        Long pid = Optional.ofNullable(variant.getProduct())
            .map(p -> p.getProductId())
            .orElseThrow(() ->
                new IllegalArgumentException("Product ID is required to create a product variant."));
        if (!productRepo.existsById(pid)) {
            throw new IllegalArgumentException("Product not found. Cannot create product variant.");
        }
        return variantRepo.save(variant);
    }

    public ProductVariant updateProductVariant(Long id, ProductVariant variant) {
        if (!id.equals(variant.getVariantId())) {
            throw new IllegalArgumentException("Variant ID in URL and body must match.");
        }
        findById(id);

        Long pid = Optional.ofNullable(variant.getProduct())
            .map(p -> p.getProductId())
            .orElseThrow(() ->
                new IllegalArgumentException("Product ID is required to update a product variant."));
        if (!productRepo.existsById(pid)) {
            throw new IllegalArgumentException("Product not found. Cannot update product variant.");
        }

        variant.setVariantId(id);
        return variantRepo.save(variant);
    }

    public ProductVariant partialUpdateProductVariant(Long id, Map<String, Object> updates) {
        ProductVariant existing = findById(id);
        BeanWrapper wrapper = new BeanWrapperImpl(existing);

        updates.forEach((prop, val) -> {
            if ("price".equals(prop) && val instanceof Number) {
                wrapper.setPropertyValue(prop, BigDecimal.valueOf(((Number) val).doubleValue()));
            } else {
                wrapper.setPropertyValue(prop, val);
            }
        });

        return variantRepo.save(existing);
    }

    public void deleteById(Long id) {
        ProductVariant productVariant = variantRepo.findById(id)
        .orElseThrow(() ->
            new ResourceNotFoundException("Product Variant not found with ID: " + id));
        variantRepo.delete(productVariant);
    }
}
