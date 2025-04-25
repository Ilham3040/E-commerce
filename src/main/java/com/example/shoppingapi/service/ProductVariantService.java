package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.ProductVariantCreateDTO;
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

    public ProductVariant saveProductVariant(ProductVariantCreateDTO productVariantCreateDTO) {
        productRepo.findById(productVariantCreateDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product with ID : " + productVariantCreateDTO.getProductId() + " not  found cannot create Product Variant"));

        ProductVariant productVariant = ProductVariant.builder()
                .product(Product.builder().productId(productVariantCreateDTO.getProductId()).build())
                .variantName(productVariantCreateDTO.getVariantName())
                .price(productVariantCreateDTO.getPrice())
                .stockQuantity(productVariantCreateDTO.getStockQuantity())
                .build();

        return variantRepo.save(productVariant);
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

    public void deleteById(Long id) {
        ProductVariant productVariant = getProductVariantById(id);
        variantRepo.delete(productVariant);
    }
}
