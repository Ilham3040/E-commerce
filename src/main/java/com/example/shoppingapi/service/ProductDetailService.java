package com.example.shoppingapi.service;

import com.example.shoppingapi.model.ProductDetail;
import com.example.shoppingapi.repository.ProductDetailRepository;
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
public class ProductDetailService {

    @Autowired
    private ProductDetailRepository productDetailRepository;
    
    @Autowired
    private ProductRepository productRepository;

    public List<ProductDetail> findAll() {
        return productDetailRepository.findAll();
    }

    public Optional<ProductDetail> findById(Long id) {
        return productDetailRepository.findById(id);
    }

    public Optional<ProductDetail> findByProductId(Long id) {
        return productDetailRepository.findProductDetailbyProductId(id);
    }

    public ProductDetail saveProductDetail(ProductDetail productDetail) {
        if (productDetail.getProduct() == null || productDetail.getProduct().getProductId() == null) {
            throw new IllegalArgumentException("Product ID is required to create a product detail.");
        }
    
        boolean exists = productRepository.existsById(productDetail.getProduct().getProductId());
        if (!exists) {
            throw new IllegalArgumentException("Product not found. Cannot create product detail.");
        }
    
        return productDetailRepository.save(productDetail);
    }

    public ProductDetail updateProductDetail(Long id, ProductDetail productDetail) {
        if (!id.equals(productDetail.getProductDetailId())) {
            throw new IllegalArgumentException("User ID in URL and body must match.");
        }
        Optional<ProductDetail> existingProductDetailOpt = productDetailRepository.findById(id);
        if (existingProductDetailOpt.isEmpty()) {
            throw new IllegalArgumentException("ProductDetail not found with ID: " + id);
        }

        Optional<ProductDetail> product = productDetailRepository.findById(productDetail.getProduct().getProductId());
        if (product.isEmpty()) {
            throw new IllegalArgumentException("User not found. Cannot create product detail.");
        }
        ProductDetail updatedProductDetail = productDetail;
        updatedProductDetail.setProductDetailId(id);

        return productDetailRepository.save(updatedProductDetail);
    }

    
    public ProductDetail partialUpdateProductDetail(Long id, Map<String, Object> updates) {
        Optional<ProductDetail> existingProductDetailOpt = productDetailRepository.findById(id);
        if (existingProductDetailOpt.isEmpty()) {
            throw new IllegalArgumentException("ProductDetail not found with ID: " + id);
        }
        
        ProductDetail existingProductDetail = existingProductDetailOpt.get();
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(ProductDetail.class, key);
            if (field != null) {
                field.setAccessible(true);
                    if ("reviewRating".equals(key) && value instanceof Number) {
                    BigDecimal reviewRatings = new BigDecimal(value.toString());
                    ReflectionUtils.setField(field, existingProductDetail, reviewRatings);
                } else {
                    ReflectionUtils.setField(field, existingProductDetail, value);
                }

            }
        });

        return productDetailRepository.save(existingProductDetail);
    }

    public ProductDetail deleteById(Long id) {
        ProductDetail productDetail = productDetailRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product Detail not found with ID: " + id));

        productDetail.setDeletedAt(LocalDateTime.now());
        productDetailRepository.save(productDetail);
        return productDetail;
    }

}
