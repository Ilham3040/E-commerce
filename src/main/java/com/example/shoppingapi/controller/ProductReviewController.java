package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.create.ProductReviewCreateDTO;
import com.example.shoppingapi.dto.response.ProductReviewDTO;
import com.example.shoppingapi.dto.put.ProductReviewPutDTO;
import com.example.shoppingapi.dto.patch.ProductReviewPatchDTO;
import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.model.ProductReview;
import com.example.shoppingapi.service.ProductReviewService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productsreview")
@RequiredArgsConstructor
public class ProductReviewController {
    private final ProductReviewService productReviewService;

    @GetMapping
    public ApiResponse<List<ProductReviewDTO>> getAllProductReviews() {
        List<ProductReviewDTO> productReviewDTOs = productReviewService.findAll()
                .stream()
                .map(review -> new ProductReviewDTO(review.getReviewId(), review.getUser().getUserId(), review.getProduct().getProductId()))
                .collect(Collectors.toList());
        return new ApiResponse<>("Successfully fetched all product reviews", productReviewDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductReview> getProductReviewById(@PathVariable Long id) {
        ProductReview review = productReviewService.getProductReviewById(id);
        return new ApiResponse<>("Successfully fetched product review", review, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<ProductReviewDTO> createProductReview(@Validated @RequestBody ProductReviewCreateDTO productReviewCreateDTO) {
        ProductReview createdReview = productReviewService.saveProductReview(productReviewCreateDTO);
        return new ApiResponse<>("Successfully created product review", new ProductReviewDTO(createdReview.getReviewId(), createdReview.getUser().getUserId(), createdReview.getProduct().getProductId()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductReviewDTO> updateProductReview(@PathVariable Long id, @Validated @RequestBody ProductReviewPutDTO productReviewPutDTO) {
        ProductReview updatedReview = productReviewService.updateProductReview(id, productReviewPutDTO);
        return new ApiResponse<>("Successfully updated product review", new ProductReviewDTO(updatedReview.getReviewId(), updatedReview.getUser().getUserId(), updatedReview.getProduct().getProductId()), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ApiResponse<ProductReviewDTO> updatePartiallyProductReview(@PathVariable Long id, @RequestBody ProductReviewPatchDTO productReviewPatchDTO) {
        ProductReview updatedReview = productReviewService.partiallyUpdateProductReview(id, productReviewPatchDTO);
        return new ApiResponse<>("Successfully updated product review", new ProductReviewDTO(updatedReview.getReviewId(), updatedReview.getUser().getUserId(), updatedReview.getProduct().getProductId()), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProductReview(@PathVariable Long id) {
        productReviewService.deleteById(id);
        return new ApiResponse<>("Successfully deleted product review", null, HttpStatus.NO_CONTENT);
    }
}
