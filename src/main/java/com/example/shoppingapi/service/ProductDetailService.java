package com.example.shoppingapi.service;

import com.example.shoppingapi.model.ProductDetail;
import com.example.shoppingapi.repository.ProductDetailRepository;
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
public class ProductDetailService {

    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository       productRepository;

    public List<ProductDetail> findAll() {
        return productDetailRepository.findAll();
    }

    public ProductDetail findById(Long id) {
        return productDetailRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("ProductDetail not found with ID: " + id));
    }

    public ProductDetail findByProductId(Long productId) {
        return productDetailRepository.findProductDetailbyProductId(productId)
            .orElseThrow(() ->
                new ResourceNotFoundException("ProductDetail not found for product ID: " + productId));
    }

    public ProductDetail saveProductDetail(ProductDetail detail) {
        Long productId = Optional.ofNullable(detail.getProduct())
            .map(p -> p.getProductId())
            .orElseThrow(() ->
                new IllegalArgumentException("Product ID is required to create a product detail."));

        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("Product not found. Cannot create product detail.");
        }
        return productDetailRepository.save(detail);
    }

    public ProductDetail updateProductDetail(Long id, ProductDetail detail) {
        if (!id.equals(detail.getProductDetailId())) {
            throw new IllegalArgumentException("ProductDetail ID in URL and body must match.");
        }
        findById(id);  // throws if missing
        detail.setProductDetailId(id);
        return productDetailRepository.save(detail);
    }

    public ProductDetail partialUpdateProductDetail(Long id, Map<String, Object> updates) {
        ProductDetail existing = findById(id);

        BeanWrapper wrapper = new BeanWrapperImpl(existing);
        updates.forEach((prop, val) -> {
            if ("reviewRating".equals(prop) && val instanceof Number) {
                wrapper.setPropertyValue(prop, new BigDecimal(val.toString()));
            } else {
                wrapper.setPropertyValue(prop, val);
            }
        });

        return productDetailRepository.save(existing);
    }

    public void deleteById(Long id) {
        ProductDetail existing = productDetailRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product Detail not found"));
        productDetailRepository.delete(existing);
    }
}
