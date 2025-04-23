package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.patch.ProductReviewPatchDTO;
import com.example.shoppingapi.dto.put.ProductReviewPutDTO;
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
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductReviewService {

    private final ProductReviewRepository reviewRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public List<ProductReview> findAll() {
        return reviewRepo.findAll();
    }

    public ProductReview getProductReviewById(Long id) {
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

    // Update method for ProductReviewService
    public ProductReview updateProductReview(Long id, ProductReviewPutDTO productReviewPutDTO) {
        ProductReview existingProductReview = getProductReviewById(id);
        ReflectionUtils.doWithFields(ProductReviewPutDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(productReviewPutDTO);
            if (value != null) {
                Field productReviewField = ReflectionUtils.findField(ProductReview.class, field.getName());
                if (productReviewField != null) {
                    productReviewField.setAccessible(true);
                    productReviewField.set(existingProductReview, value);
                }
            }
        });
        return reviewRepo.save(existingProductReview);
    }

    // Partial update method for ProductReviewService
    public ProductReview updateProductReview(Long id, ProductReviewPatchDTO productReviewPatchDTO) {
        ProductReview existingProductReview = getProductReviewById(id);
        ReflectionUtils.doWithFields(ProductReviewPatchDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(productReviewPatchDTO);
            if (value != null) {
                Field productReviewField = ReflectionUtils.findField(ProductReview.class, field.getName());
                if (productReviewField != null) {
                    productReviewField.setAccessible(true);
                    productReviewField.set(existingProductReview, value);
                }
            }
        });
        return reviewRepo.save(existingProductReview);
    }

    public void deleteProductReviewById(Long id) {
        ProductReview productReview = getProductReviewById(id);
        reviewRepo.delete(productReview);
    }
}
