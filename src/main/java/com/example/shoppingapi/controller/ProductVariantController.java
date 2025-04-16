package com.example.shoppingapi.controller;

import com.example.shoppingapi.model.ProductVariant;
import com.example.shoppingapi.service.ProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.dto.response.ProductVariantDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-variants")
public class ProductVariantController {

    @Autowired
    private ProductVariantService productVariantService;

    @GetMapping
    public List<ProductVariant> getAllProductVariants() {
        return productVariantService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<ProductVariant> getProductVariantById(@PathVariable Long id) {
        return productVariantService.findById(id);
    }

    @GetMapping("/product/{id}")
    public Optional<ProductVariant> getProductVariantByProductId(@PathVariable Long id) {
        return productVariantService.findByProductId(id);
    }

    @PostMapping
    public ResponseEntity<ProductVariantDTO> createProductVariant(@RequestBody ProductVariant productVariant) {
        ProductVariant savedProductVariant = productVariantService.saveProductVariant(productVariant);

        ProductVariantDTO productVariantDTO = new ProductVariantDTO(savedProductVariant.getVariantId(), savedProductVariant.getProduct().getProductId());

        return ResponseEntity.ok(productVariantDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductVariantDTO>> updateProductVariant(
            @PathVariable Long id,
            @RequestBody ProductVariant productVariant) {
        try {
            ProductVariant updatedProductVariant = productVariantService.updateProductVariant(id, productVariant);
            ProductVariantDTO productVariantDTO = new ProductVariantDTO(updatedProductVariant.getVariantId(), updatedProductVariant.getProduct().getProductId());
            ApiResponse<ProductVariantDTO> response = new ApiResponse<>("ProductVariant successfully updated", productVariantDTO);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<ProductVariantDTO> response = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Partial Update (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductVariantDTO>> partialUpdateProductVariant(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        try {
            ProductVariant updatedProductVariant = productVariantService.partialUpdateProductVariant(id, updates);
            ProductVariantDTO productVariantDTO = new ProductVariantDTO(updatedProductVariant.getVariantId(), updatedProductVariant.getProduct().getProductId());
            ApiResponse<ProductVariantDTO> response = new ApiResponse<>("ProductVariant successfully updated", productVariantDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<ProductVariantDTO> response = new ApiResponse<>("Error updating ProductVariant: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteByIdVariant(@PathVariable Long id) {
        productVariantService.deleteById(id);
    }
}
