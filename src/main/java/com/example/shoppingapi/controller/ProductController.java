package com.example.shoppingapi.controller;

import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.shoppingapi.dto.ProductDTO;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get products by store ID
    @GetMapping("/store/{storeId}")
    public List<Product> getProductsByStoreId(@PathVariable Long storeId) {
        return productService.getProductsByStoreId(storeId);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);

        // Convert to DTO before returning
        ProductDTO productDTO = new ProductDTO(savedProduct.getProductId(), savedProduct.getProductName(), savedProduct.getPrice());

        return ResponseEntity.ok(productDTO);
    }

    // Delete a product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
