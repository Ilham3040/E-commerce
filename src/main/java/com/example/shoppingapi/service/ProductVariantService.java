package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.patch.ProductVariantPatchDTO;
import com.example.shoppingapi.dto.put.ProductVariantPutDTO;
import com.example.shoppingapi.repository.ProductVariantRepository;
import com.example.shoppingapi.model.*;
import com.example.shoppingapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
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

    public ProductVariant getProductVariantById(Long id) {
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
            .map(Product::getProductId)
            .orElseThrow(() ->
                new IllegalArgumentException("Product ID is required to create a product variant."));
        if (!productRepo.existsById(pid)) {
            throw new IllegalArgumentException("Product not found. Cannot create product variant.");
        }
        return variantRepo.save(variant);
    }

    public ProductVariant updateProductVariant(Long id, ProductVariantPutDTO productVariantPutDTO) {
        ProductVariant existingProductVariant = getProductVariantById(id);
        ReflectionUtils.doWithFields(ProductVariantPutDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(productVariantPutDTO);
            if (value != null) {
                Field productVariantField = ReflectionUtils.findField(ProductVariant.class, field.getName());
                if (productVariantField != null) {
                    productVariantField.setAccessible(true);
                    productVariantField.set(existingProductVariant, value);
                }
            }
        });
        return variantRepo.save(existingProductVariant);
    }

    public ProductVariant partiallyUpdateProductVariant(Long id, ProductVariantPatchDTO productVariantPatchDTO) {
        ProductVariant existingProductVariant = getProductVariantById(id);
        ReflectionUtils.doWithFields(ProductVariantPatchDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(productVariantPatchDTO);
            if (value != null) {
                Field productVariantField = ReflectionUtils.findField(ProductVariant.class, field.getName());
                if (productVariantField != null) {
                    productVariantField.setAccessible(true);
                    productVariantField.set(existingProductVariant, value);
                }
            }
        });
        return variantRepo.save(existingProductVariant);
    }

    public void deleteProductVariantById(Long id) {
        ProductVariant productVariant = getProductVariantById(id);
        variantRepo.delete(productVariant);
    }
}
