package com.example.shoppingapi.service;

import com.example.shoppingapi.model.ProductVariant;
import com.example.shoppingapi.repository.ProductVariantRepository;
import com.example.shoppingapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductVariantService {

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<ProductVariant> findAll() {
        return productVariantRepository.findAll();
    }

    public Optional<ProductVariant> findById(Long id) {
        return productVariantRepository.findById(id);
    }

    public Optional<ProductVariant> findByProductId(Long id) {
        return productVariantRepository.findProductVariantbyProductId(id);
    }

    public ProductVariant saveProductVariant(ProductVariant productVariant) {
        if (productVariant.getProduct() == null || productVariant.getProduct().getProductId() == null) {
            throw new IllegalArgumentException("Product ID is required to create a product variant.");
        }

        boolean exists = productRepository.existsById(productVariant.getProduct().getProductId());
        if (!exists) {
            throw new IllegalArgumentException("Product not found. Cannot create product variant.");
        }

        return productVariantRepository.save(productVariant);
    }

    public ProductVariant updateProductVariant(Long id, ProductVariant productVariant) {
        if (!id.equals(productVariant.getVariantId())) {
            throw new IllegalArgumentException("Variant ID in URL and body must match.");
        }
        Optional<ProductVariant> existingProductVariantOpt = productVariantRepository.findById(id);
        if (existingProductVariantOpt.isEmpty()) {
            throw new IllegalArgumentException("ProductVariant not found with ID: " + id);
        }
        if (productVariant.getProduct() == null || productVariant.getProduct().getProductId() == null) {
            throw new IllegalArgumentException("Product ID is required to update a product variant.");
        }

        Optional<ProductVariant> product = productVariantRepository.findById(productVariant.getProduct().getProductId());
        if (product.isEmpty()) {
            throw new IllegalArgumentException("Product not found. Cannot update product variant.");
        }
        ProductVariant updatedProductVariant = productVariant;
        updatedProductVariant.setVariantId(id);

        return productVariantRepository.save(updatedProductVariant);
    }

    public ProductVariant partialUpdateProductVariant(Long id, Map<String, Object> updates) {
        Optional<ProductVariant> existingProductVariantOpt = productVariantRepository.findById(id);
        if (existingProductVariantOpt.isEmpty()) {
            throw new IllegalArgumentException("ProductVariant not found with ID: " + id);
        }

        ProductVariant existingProductVariant = existingProductVariantOpt.get();
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(ProductVariant.class, key);
            if (field != null) {
                field.setAccessible(true);
                if ("reviewRating".equals(key) && value instanceof Number) {
                    BigDecimal reviewRatings = new BigDecimal(value.toString());
                    ReflectionUtils.setField(field, existingProductVariant, reviewRatings);
                } else {
                    ReflectionUtils.setField(field, existingProductVariant, value);
                }
            }
        });

        return productVariantRepository.save(existingProductVariant);
    }

    public ProductVariant deleteById(Long id) {
        ProductVariant productVariant = productVariantRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ProductVariant not found with ID: " + id));

        productVariant.setDeletedAt(LocalDateTime.now());
        productVariantRepository.save(productVariant);
        return productVariant;
    }

}
