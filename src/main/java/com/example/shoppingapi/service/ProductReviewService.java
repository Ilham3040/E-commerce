package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.ProductReview;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.ProductReviewRepository;
import com.example.shoppingapi.repository.UserRepository;
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
public class ProductReviewService {

    @Autowired
    private ProductReviewRepository productReviewRepository;


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ProductReview> findAll() {
        return productReviewRepository.findAll();
    }

    public Optional<ProductReview> findById(Long id) {
        return productReviewRepository.findById(id);
    }


    public ProductReview saveProductReview(ProductReview productReview) {
        if (productReview.getProduct() == null || productReview.getProduct().getProductId() == null) {
            throw new IllegalArgumentException("Product ID is required to create a product review.");
        }
    
        Optional<Product> productexists = productRepository.findById(productReview.getProduct().getProductId());
        if (productexists.isEmpty()) {
            throw new IllegalArgumentException("Product not found. Cannot create product review.");
        }

        Optional<User> userexists = userRepository.findById(productReview.getUser().getUserId());
        if (userexists.isEmpty()) {
            throw new IllegalArgumentException("User not found. Cannot create product review.");
        }
    
        return productReviewRepository.save(productReview);
    }

    public ProductReview updateProductReview(Long id, ProductReview productReview) {
        if (!id.equals(productReview.getReviewId())) {
            throw new IllegalArgumentException("Review ID in URL and body must match.");
        }
        Optional<ProductReview> existingProductReviewOpt = productReviewRepository.findById(id);
        if (existingProductReviewOpt.isEmpty()) {
            throw new IllegalArgumentException("ProductReview not found with ID: " + id);
        }

        Optional<User> user = userRepository.findById(productReview.getUser().getUserId());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found. Cannot create product review.");
        }

        Optional<Product> product = productRepository.findById(productReview.getProduct().getProductId());
        if (product.isEmpty()) {
            throw new IllegalArgumentException("Product not found. Cannot create product review.");
        }
        ProductReview updatedProductReview = productReview;
        updatedProductReview.setReviewId(id);

        return productReviewRepository.save(updatedProductReview);
    }

    
    public ProductReview partialUpdateProductReview(Long id, Map<String, Object> updates) {
        Optional<ProductReview> existingProductReviewOpt = productReviewRepository.findById(id);
        if (existingProductReviewOpt.isEmpty()) {
            throw new IllegalArgumentException("ProductReview not found with ID: " + id);
        }
        
        ProductReview existingProductReview = existingProductReviewOpt.get();
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(ProductReview.class, key);
            if (field != null) {
                field.setAccessible(true);
                    if ("reviewRating".equals(key) && value instanceof Number) {
                    BigDecimal reviewRatings = new BigDecimal(value.toString());
                    ReflectionUtils.setField(field, existingProductReview, reviewRatings);
                } else {
                    ReflectionUtils.setField(field, existingProductReview, value);
                }

            }
        });

        return productReviewRepository.save(existingProductReview);
    }

    public ProductReview deleteById(Long id) {
        ProductReview productReview = productReviewRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ProductReview not found with ID: " + id));

        productReview.setDeletedAt(LocalDateTime.now());
        productReviewRepository.save(productReview);
        return productReview;
    }

}
