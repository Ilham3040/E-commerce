package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.create.ProductDetailCreateDTO;
import com.example.shoppingapi.dto.detailed.DetailedProductDetailDTO;
import com.example.shoppingapi.dto.put.ProductDetailPutDTO;
import com.example.shoppingapi.dto.patch.ProductDetailPatchDTO;
import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.dto.response.ProductDetailDTO;
import com.example.shoppingapi.model.ProductDetail;
import com.example.shoppingapi.service.ProductDetailService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productsdetails/")
@RequiredArgsConstructor
public class ProductDetailController {
    private final ProductDetailService productDetailService;

    @GetMapping
    public ApiResponse<List<ProductDetailDTO>> getAllProductDetails() {
        List<ProductDetailDTO> productDetailDTOs = productDetailService.findAll()
                .stream()
                .map(detail -> new ProductDetailDTO(detail.getProductDetailId(), detail.getProduct().getProductId()))
                .collect(Collectors.toList());
        return new ApiResponse<>("Successfully fetched all product details", productDetailDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ApiResponse<DetailedProductDetailDTO> getProductDetailById(@PathVariable Long id) {
        ProductDetail productDetail = productDetailService.getProductDetailByProductId(id);
        return new ApiResponse<>("Successfully fetched product detail", DetailedProductDetailDTO.builder()
                .productDetailId(productDetail.getProductDetailId())
                .productId(productDetail.getProduct().getProductId())
                .description(productDetail.getDescription())
                .images(productDetail.getAttachmentUrls())
                .reviewRating(productDetail.getReviewRating())
                .totalSold(productDetail.getTotalSold())
                .createdAt(productDetail.getCreatedAt())
                .updatedAt(productDetail.getUpdatedAt())
                .build(), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<ProductDetailDTO> createProductDetail(@Validated @RequestBody ProductDetailCreateDTO productDetailCreateDTO) {
        ProductDetail createdProductDetail = productDetailService.saveProductDetail(productDetailCreateDTO);
        return new ApiResponse<>("Successfully created product detail", new ProductDetailDTO(createdProductDetail.getProductDetailId(), createdProductDetail.getProduct().getProductId()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductDetailDTO> updateProductDetail(@PathVariable Long id, @Validated @RequestBody ProductDetailPutDTO productDetailPutDTO) {
        ProductDetail updatedProductDetail = productDetailService.updateProductDetail(id, productDetailPutDTO);
        return new ApiResponse<>("Successfully updated product detail", new ProductDetailDTO(updatedProductDetail.getProductDetailId(), updatedProductDetail.getProduct().getProductId()), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ApiResponse<ProductDetailDTO> partialUpdateProductDetail(@PathVariable Long id, @RequestBody ProductDetailPatchDTO productDetailPatchDTO) {
        ProductDetail updatedProductDetail = productDetailService.partialUpdateProductDetail(id, productDetailPatchDTO);
        return new ApiResponse<>("Successfully partially updated product detail", new ProductDetailDTO(updatedProductDetail.getProductDetailId(), updatedProductDetail.getProduct().getProductId()), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProductDetail(@PathVariable Long id) {
        productDetailService.deleteByProductId(id);
        return new ApiResponse<>("Successfully deleted product detail", null, HttpStatus.NO_CONTENT);
    }
}
