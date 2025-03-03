package com.example.shoppingapi.controller;

import com.example.shoppingapi.model.ProductDetail;
import com.example.shoppingapi.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.shoppingapi.dto.ProductDetailDTO;

import java.util.List;
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
    @PostMapping
    public ResponseEntity<ProductDetailDTO> createProduct(@RequestBody ProductDetail product) {
        ProductDetail savedProduct = productDetailService.saveProductDetail(product);

        ProductDetailDTO productDTO = new ProductDetailDTO(savedProduct.getProductDetailId(), savedProduct.getProduct().getProductId());

        return ResponseEntity.ok(productDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteProductDetail(@PathVariable Long id) {
        productDetailService.deleteById(id);
    }
}
