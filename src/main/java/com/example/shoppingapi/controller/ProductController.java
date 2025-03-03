package com.example.shoppingapi.controller;

import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.shoppingapi.dto.ApiResponse;
import com.example.shoppingapi.dto.ProductDTO;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    
    @GetMapping("/store/{storeId}")
    public List<Product> getProductsByStoreId(@PathVariable Long storeId) {
        return productService.getProductsByStoreId(storeId);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);

        
        ProductDTO productDTO = new ProductDTO(savedProduct.getProductId(), savedProduct.getStore().getStoreId());

        ApiResponse<ProductDTO> response = new ApiResponse<>("Product sucessfully added", productDTO);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
