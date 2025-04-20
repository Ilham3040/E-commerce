package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.request.ProductRequestDTO;
import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.dto.response.ProductDTO;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.service.ProductService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ApiResponse<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> dtos = productService.getAllProducts()
            .stream()
            .map(p -> new ProductDTO(p.getProductId(), p.getStore().getStoreId()))
            .collect(Collectors.toList());
        return new ApiResponse<>("Fetched all products", dtos);
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDTO> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return new ApiResponse<>(
            "Fetched product",
            new ProductDTO(product.getProductId(), product.getStore().getStoreId())
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProductDTO> createProduct(@Validated @RequestBody ProductRequestDTO dto) {
        Product toCreate = Product.builder()
            .productName(dto.getProductName())
            .price(dto.getPrice())
            .store(Store.builder().storeId(dto.getStoreId()).build())
            .build();
        Product created = productService.saveProduct(toCreate);
        return new ApiResponse<>("Product created", new ProductDTO(created.getProductId(), created.getStore().getStoreId()));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductDTO> updateProduct(
        @PathVariable Long id,
        @Validated @RequestBody ProductRequestDTO dto
    ) {
        Product toUpdate = Product.builder()
            .productId(id)
            .productName(dto.getProductName())
            .price(dto.getPrice())
            .store(Store.builder().storeId(dto.getStoreId()).build())
            .build();
        Product updated = productService.updateProduct(id, toUpdate);
        return new ApiResponse<>("Product updated", new ProductDTO(updated.getProductId(), updated.getStore().getStoreId()));
    }

    @PatchMapping("/{id}")
    public ApiResponse<ProductDTO> partialUpdateProduct(
        @PathVariable Long id,
        @RequestBody Map<String,Object> updates
    ) {
        Product updated = productService.partialUpdateProduct(id, updates);
        return new ApiResponse<>("Product partially updated", new ProductDTO(updated.getProductId(), updated.getStore().getStoreId()));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return new ApiResponse<>("Product deleted", null);
    }
}
