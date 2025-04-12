package com.example.shoppingapi.controller;

import com.example.shoppingapi.model.ProductDetail;
import com.example.shoppingapi.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.shoppingapi.dto.ApiResponse;
import com.example.shoppingapi.dto.ProductDetailDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-details")
public class ProductDetailController {

    @Autowired
    private ProductDetailService productDetailService;

    @GetMapping
    public List<ProductDetail> getAllProductDetails() {
        return productDetailService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<ProductDetail> getProductDetailById(@PathVariable Long id) {
        return productDetailService.findById(id);
    }

    @GetMapping("/product/{id}")
    public Optional<ProductDetail> getProductDetailbyProductId(@PathVariable Long id) {
        return productDetailService.findByProductId(id);
    }

    @PostMapping
    public ResponseEntity<ProductDetailDTO> createProduct(@RequestBody ProductDetail product) {
        ProductDetail savedProduct = productDetailService.saveProductDetail(product);

        ProductDetailDTO productDTO = new ProductDetailDTO(savedProduct.getProductDetailId(), savedProduct.getProduct().getProductId());

        return ResponseEntity.ok(productDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDetailDTO>> updateProductDetail(
            @PathVariable Long id,
            @RequestBody ProductDetail productDetail) {
        try {
            ProductDetail updatedProductDetail = productDetailService.updateProductDetail(id,productDetail);
            ProductDetailDTO ProductDetailDTO = new ProductDetailDTO(updatedProductDetail.getProductDetailId(), updatedProductDetail.getProduct().getProductId());
            ApiResponse<ProductDetailDTO> response = new ApiResponse<>("ProductDetail successfully updated", ProductDetailDTO);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<ProductDetailDTO> response = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Partial Update (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDetailDTO>> partialUpdateProductDetail(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        try {
            ProductDetail updatedProductDetail = productDetailService.partialUpdateProductDetail(id, updates);
            ProductDetailDTO ProductDetailDTO = new ProductDetailDTO(updatedProductDetail.getProductDetailId(), updatedProductDetail.getProduct().getProductId());
            ApiResponse<ProductDetailDTO> response = new ApiResponse<>("ProductDetail successfully updated", ProductDetailDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<ProductDetailDTO> response = new ApiResponse<>("Error updating ProductDetail: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    

    @DeleteMapping("/{id}")
    public void deleteProductDetail(@PathVariable Long id) {
        productDetailService.deleteById(id);
    }
}
