package com.example.shoppingapi.service;

import com.example.shoppingapi.model.ProductDetail;
import com.example.shoppingapi.repository.ProductDetailRepository;
import com.example.shoppingapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
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
        Optional<ProductDetail> existingProductDetailOpt = productDetailRepository.findById(id);
        if (existingProductDetailOpt.isEmpty()) {
            throw new IllegalArgumentException("ProductDetail not found with ID: " + id);
        }
        if (productDetail.getProduct() == null || productDetail.getProduct().getProductId() == null) {
            throw new IllegalArgumentException("User ID is required to create a product detail.");
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

    
    

    public void deleteById(Long id) {
        productDetailRepository.deleteById(id);
    }
}
