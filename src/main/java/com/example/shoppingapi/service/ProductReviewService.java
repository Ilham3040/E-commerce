package com.example.shoppingapi.service;

import com.example.shoppingapi.model.ProductReview;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.ProductReviewRepository;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductReviewService {

    private final ProductReviewRepository reviewRepo;
    private final ProductRepository       productRepo;
    private final UserRepository          userRepo;

    public List<ProductReview> findAll() {
        return reviewRepo.findAll();
    }

    public ProductReview findById(Long id) {
        return reviewRepo.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("ProductReview not found with ID: " + id));
    }

    public ProductReview saveProductReview(ProductReview review) {
        Long productId = Optional.ofNullable(review.getProduct())
            .map(Product::getProductId)
            .orElseThrow(() ->
                new IllegalArgumentException("Product ID is required to create a product review."));
        if (!productRepo.existsById(productId)) {
            throw new IllegalArgumentException("Product not found. Cannot create product review.");
        }

        Long userId = Optional.ofNullable(review.getUser())
            .map(User::getUserId)
            .orElseThrow(() ->
                new IllegalArgumentException("User ID is required to create a product review."));
        userRepo.findById(userId)
            .orElseThrow(() ->
                new IllegalArgumentException("User not found. Cannot create product review."));

        return reviewRepo.save(review);
    }

    public ProductReview updateProductReview(Long id, ProductReview review) {
        if (!id.equals(review.getReviewId())) {
            throw new IllegalArgumentException("Review ID in URL and body must match.");
        }
        findById(id);  // ensure exists

        Long userId = Optional.ofNullable(review.getUser())
            .map(User::getUserId)
            .orElseThrow(() ->
                new IllegalArgumentException("User ID is required to update a product review."));
        userRepo.findById(userId)
            .orElseThrow(() ->
                new IllegalArgumentException("User not found. Cannot update product review."));

        Long productId = Optional.ofNullable(review.getProduct())
            .map(Product::getProductId)
            .orElseThrow(() ->
                new IllegalArgumentException("Product ID is required to update a product review."));
        if (!productRepo.existsById(productId)) {
            throw new IllegalArgumentException("Product not found. Cannot update product review.");
        }

        review.setReviewId(id);
        return reviewRepo.save(review);
    }

    public ProductReview partialUpdateProductReview(Long id, Map<String, Object> updates) {
        ProductReview existing = findById(id);

        BeanWrapper wrapper = new BeanWrapperImpl(existing);
        updates.forEach((prop, val) -> {
            if ("starRating".equals(prop) && val instanceof Number) {
                wrapper.setPropertyValue(prop, new BigDecimal(val.toString()));
            } else {
                wrapper.setPropertyValue(prop, val);
            }
        });

        return reviewRepo.save(existing);
    }

    public ProductReview softDeleteProductReview(Long id) {
        ProductReview existing = findById(id);
        existing.setDeletedAt(LocalDateTime.now());
        return reviewRepo.save(existing);
    }
}
