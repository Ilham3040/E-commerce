package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.request.ProductDetailRequestDTO;
import com.example.shoppingapi.dto.response.ProductDetailDTO;
import com.example.shoppingapi.model.*;
import com.example.shoppingapi.service.ProductDetailService;
import com.example.shoppingapi.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productsdetail")
@RequiredArgsConstructor
public class ProductDetailController {
    private final ProductDetailService productDetailService;

    @GetMapping
    public ApiResponse<List<ProductDetailDTO>> getAllProductDetail(){
        List<ProductDetailDTO> datas = productDetailService.findAll()
            .stream()
            .map(detail -> new ProductDetailDTO(detail.getProductDetailId(), detail.getProduct().getProductId()))
            .collect(Collectors.toList());
        return new ApiResponse<>("Successfully fetched all ProductDetail", datas,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDetail> getDetailById(@PathVariable Long id){
        ProductDetail data = productDetailService.findById(id);
        return new ApiResponse<>("Successfully fetch data", data,HttpStatus.OK);
    }

    @PostMapping
    public ApiResponse<ProductDetailDTO> createProductDetail(@Validated @RequestBody ProductDetailRequestDTO dto){
        ProductDetail wannabe = ProductDetail.builder()
            .product(Product.builder().productId(dto.getProductId()).build())
            .description(dto.getDescription())
            .build();
        
        ProductDetail created = productDetailService.saveProductDetail(wannabe);
        return new ApiResponse<>("Successfully creating store detail",
            new ProductDetailDTO(created.getProductDetailId(), created.getProduct().getProductId()),HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductDetailDTO> updateEntireProductDetail(@PathVariable Long id, @Validated @RequestBody ProductDetailRequestDTO dto)
    {
        ProductDetail wannabe = ProductDetail.builder()
            .productDetailId(id)
            .product(Product.builder().productId(dto.getProductId()).build())
            .description(dto.getDescription())
            .build();

        ProductDetail updated = productDetailService.updateProductDetail(id, wannabe);
        return new ApiResponse<>("Successfully creating store detail",
            new ProductDetailDTO(updated.getProductDetailId(), updated.getProduct().getProductId()),HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ApiResponse<ProductDetailDTO> updatePartiallyDetail(@PathVariable Long id, @RequestBody Map<String,Object> updates)
    {
        ProductDetail updated = productDetailService.partialUpdateProductDetail(id, updates);
        return new ApiResponse<>("Successfully creating store detail",
            new ProductDetailDTO(updated.getProductDetailId(), updated.getProduct().getProductId()),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<ProductDetail> deleteProductDetail(@PathVariable Long id){
        productDetailService.deleteById(id);
        return new ApiResponse<>("Successfully deleted detail with ID :" + id,null,HttpStatus.NO_CONTENT);
    }
    
}
