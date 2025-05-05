package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.create.ProductVariantCreateDTO;
import com.example.shoppingapi.dto.detailed.DetailedProductVariantDTO;
import com.example.shoppingapi.dto.put.ProductVariantPutDTO;
import com.example.shoppingapi.dto.patch.ProductVariantPatchDTO;
import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.dto.response.ProductVariantDTO;
import com.example.shoppingapi.model.ProductVariant;
import com.example.shoppingapi.service.ProductVariantService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productvariants/")
@RequiredArgsConstructor
public class ProductVariantController {
    private final ProductVariantService productVariantService;

    @GetMapping
    public ApiResponse<List<ProductVariantDTO>> getAllProductVariants() {
        List<ProductVariantDTO> productVariantDTOs = productVariantService.findAll()
                .stream()
                .map(variant -> new ProductVariantDTO(variant.getVariantId(), variant.getProduct().getProductId()))
                .collect(Collectors.toList());
        return new ApiResponse<>("Successfully fetched all product variants", productVariantDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ApiResponse<DetailedProductVariantDTO> getProductVariantById(@PathVariable Long id) {
        ProductVariant productVariant = productVariantService.getProductVariantById(id);
        return new ApiResponse<>("Successfully fetched product variant", DetailedProductVariantDTO.builder()
                .variantId(productVariant.getVariantId())
                .variantName(productVariant.getVariantName())
                .productId(productVariant.getProduct().getProductId())
                .price(productVariant.getPrice())
                .totalSold(productVariant.getTotalSold())
                .stockQuantity(productVariant.getStockQuantity())
                .createdAt(productVariant.getCreatedAt())
                .updatedAt(productVariant.getUpdatedAt())
                .build(), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<ProductVariantDTO> createProductVariant(@Validated @RequestBody ProductVariantCreateDTO productVariantCreateDTO) {
        ProductVariant createdProductVariant = productVariantService.saveProductVariant(productVariantCreateDTO);
        return new ApiResponse<>("Successfully created product variant", new ProductVariantDTO(createdProductVariant.getVariantId(), createdProductVariant.getProduct().getProductId()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductVariantDTO> updateProductVariant(@PathVariable Long id, @Validated @RequestBody ProductVariantPutDTO productVariantPutDTO) {
        ProductVariant updatedProductVariant = productVariantService.updateProductVariant(id, productVariantPutDTO);
        return new ApiResponse<>("Successfully updated product variant", new ProductVariantDTO(updatedProductVariant.getVariantId(), updatedProductVariant.getProduct().getProductId()), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ApiResponse<ProductVariantDTO> partialUpdateProductVariant(@PathVariable Long id, @RequestBody ProductVariantPatchDTO productVariantPatchDTO) {
        ProductVariant updatedProductVariant = productVariantService.partiallyUpdateProductVariant(id, productVariantPatchDTO);
        return new ApiResponse<>("Successfully partially updated product variant", new ProductVariantDTO(updatedProductVariant.getVariantId(), updatedProductVariant.getProduct().getProductId()), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProductVariant(@PathVariable Long id) {
        productVariantService.deleteById(id);
        return new ApiResponse<>("Successfully deleted product variant", null, HttpStatus.NO_CONTENT);
    }
}
