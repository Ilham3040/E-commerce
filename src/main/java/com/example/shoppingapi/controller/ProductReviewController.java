package com.example.shoppingapi.controller;

import com.example.shoppingapi.model.ProductReview;
import com.example.shoppingapi.service.ProductReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.dto.response.ProductReviewDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-reviews")
public class ProductReviewController {

    @Autowired
    private ProductReviewService productReviewService;

    @GetMapping
    public List<ProductReview> getAllProductReviews() {
        return productReviewService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<ProductReview> getProductReviewById(@PathVariable Long id) {
        return productReviewService.findById(id);
    }

    @PostMapping
    public ResponseEntity<ProductReviewDTO> createProduct(@RequestBody ProductReview product) {
        ProductReview savedProduct = productReviewService.saveProductReview(product);

        ProductReviewDTO productDTO = new ProductReviewDTO(savedProduct.getReviewId(), savedProduct.getProduct().getProductId());

        return ResponseEntity.ok(productDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductReviewDTO>> updateProductReview(
            @PathVariable Long id,
            @RequestBody ProductReview productReview) {
        try {
            ProductReview updatedProductReview = productReviewService.updateProductReview(id, productReview);
            ProductReviewDTO productReviewDTO = new ProductReviewDTO(updatedProductReview.getReviewId(), updatedProductReview.getProduct().getProductId());
            ApiResponse<ProductReviewDTO> response = new ApiResponse<>("ProductReview successfully updated", productReviewDTO);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<ProductReviewDTO> response = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Partial Update (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductReviewDTO>> partialUpdateProductReview(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        try {
            ProductReview updatedProductReview = productReviewService.partialUpdateProductReview(id, updates);
            ProductReviewDTO productReviewDTO = new ProductReviewDTO(updatedProductReview.getReviewId(), updatedProductReview.getProduct().getProductId());
            ApiResponse<ProductReviewDTO> response = new ApiResponse<>("ProductReview successfully updated", productReviewDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<ProductReviewDTO> response = new ApiResponse<>("Error updating ProductReview: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @DeleteMapping("/{id}")
    public void deleteByIdReview(@PathVariable Long id) {
        productReviewService.deleteById(id);
    }
}

