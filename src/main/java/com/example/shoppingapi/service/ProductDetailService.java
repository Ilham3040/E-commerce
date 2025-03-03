package com.example.shoppingapi.service;

import com.example.shoppingapi.model.ProductDetail;
import com.example.shoppingapi.repository.ProductDetailRepository;
import com.example.shoppingapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    

    public void deleteById(Long id) {
        productDetailRepository.deleteById(id);
    }
}
