package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.create.ProductCreateDTO;
import com.example.shoppingapi.dto.detailed.DetailedProductDTO;
import com.example.shoppingapi.dto.patch.ProductPatchDTO;
import com.example.shoppingapi.dto.put.ProductPutDTO;
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
@RequestMapping("/api/products/")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ApiResponse<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> productDTOs = productService.getAllProducts()
                .stream()
                .map(product -> new ProductDTO(product.getProductId(), product.getStore().getStoreId()))
                .collect(Collectors.toList());
        return new ApiResponse<>("Fetched all products", productDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ApiResponse<DetailedProductDTO> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return new ApiResponse<>("Fetched product", DetailedProductDTO.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .storeId(product.getStore().getStoreId())
                .price(product.getPrice())
                .totalReviews(product.getTotal_reviews())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build(), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<ProductDTO> createProduct(@Validated @RequestBody ProductCreateDTO productCreateDTO) {
        Product createdProduct = productService.saveProduct(productCreateDTO);
        return new ApiResponse<>("Product created", new ProductDTO(createdProduct.getProductId(), createdProduct.getStore().getStoreId()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductDTO> updateProduct(@PathVariable Long id, @Validated @RequestBody ProductPutDTO productPutDTO) {
        Product updatedProduct = productService.updateProduct(id, productPutDTO);
        return new ApiResponse<>("Product updated", new ProductDTO(updatedProduct.getProductId(), updatedProduct.getStore().getStoreId()), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ApiResponse<ProductDTO> partialUpdateProduct(@PathVariable Long id, @Validated @RequestBody ProductPatchDTO productPatchDTO) {
        Product updatedProduct = productService.partialUpdateProduct(id, productPatchDTO);
        return new ApiResponse<>("Product partially updated", new ProductDTO(updatedProduct.getProductId(), updatedProduct.getStore().getStoreId()), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return new ApiResponse<>("Product deleted", null, HttpStatus.NO_CONTENT);
    }
}
