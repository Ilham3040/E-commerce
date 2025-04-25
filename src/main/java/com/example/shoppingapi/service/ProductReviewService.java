package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.ProductReviewCreateDTO;
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

    public ProductReview saveProductReview(ProductReviewCreateDTO productReviewCreateDTO) {
        userRepo.findById(productReviewCreateDTO.getUserId()).orElseThrow(()->
                new ResourceNotFoundException("User not found with ID: " + productReviewCreateDTO.getUserId() + " cannot create product review"));
        productRepo.findById(productReviewCreateDTO.getProductId()).orElseThrow(()->
                new ResourceNotFoundException("Product not found with ID: " + productReviewCreateDTO.getProductId() + " cannot create product review"));

        ProductReview review = ProductReview.builder()
                .user(User.builder().userId(productReviewCreateDTO.getUserId()).build())
                .product(Product.builder().productId(productReviewCreateDTO.getProductId()).build())
                .starRating(productReviewCreateDTO.getStarRating())
                .description(productReviewCreateDTO.getDescription())
                .build();

        return reviewRepo.save(review);
    }

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

    public ProductReview partiallyUpdateProductReview(Long id, ProductReviewPatchDTO productReviewPatchDTO) {
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

    public void deleteById(Long id) {
        ProductReview productReview = getProductReviewById(id);
        reviewRepo.delete(productReview);
    }
}
