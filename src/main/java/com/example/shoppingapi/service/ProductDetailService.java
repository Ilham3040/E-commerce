package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.ProductDetailCreateDTO;
import com.example.shoppingapi.dto.patch.ProductDetailPatchDTO;
import com.example.shoppingapi.dto.put.ProductDetailPutDTO;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.ProductDetail;
import com.example.shoppingapi.repository.ProductDetailRepository;
import com.example.shoppingapi.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductDetailService {

    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository       productRepository;

    public List<ProductDetail> findAll() {
        return productDetailRepository.findAll();
    }

    public ProductDetail findById(Long id) {
        return productDetailRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("ProductDetail not found with ID: " + id));
    }

    public ProductDetail getProductDetailByProductId(Long productId) {
        return productDetailRepository.findProductDetailbyProductId(productId)
            .orElseThrow(() ->
                new ResourceNotFoundException("ProductDetail not found for product ID: " + productId));
    }

    public ProductDetail saveProductDetail(ProductDetailCreateDTO productDetailCreateDTO) {
        productRepository.findById(productDetailCreateDTO.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found. Cannot create product detail."));

        ProductDetail detail = ProductDetail.builder()
                .product(Product.builder().productId(productDetailCreateDTO.getProductId()).build())
                .description(productDetailCreateDTO.getDescription())
                .build();

        return productDetailRepository.save(detail);
    }

    // Update method for ProductDetailService
    public ProductDetail updateProductDetail(Long id, ProductDetailPutDTO productDetailPutDTO) {
        ProductDetail existingProductDetail = getProductDetailByProductId(id);
        ReflectionUtils.doWithFields(ProductDetailPutDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(productDetailPutDTO);
            if (value != null) {
                Field productDetailField = ReflectionUtils.findField(ProductDetail.class, field.getName());
                if (productDetailField != null) {
                    productDetailField.setAccessible(true);
                    productDetailField.set(existingProductDetail, value);
                }
            }
        });
        return productDetailRepository.save(existingProductDetail);
    }

    public ProductDetail partialUpdateProductDetail(Long id, ProductDetailPatchDTO productDetailPatchDTO) {
        ProductDetail existingProductDetail = getProductDetailByProductId(id);
        ReflectionUtils.doWithFields(ProductDetailPatchDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(productDetailPatchDTO);
            if (value != null) {
                Field productDetailField = ReflectionUtils.findField(ProductDetail.class, field.getName());
                if (productDetailField != null) {
                    productDetailField.setAccessible(true);
                    productDetailField.set(existingProductDetail, value);
                }
            }
        });
        return productDetailRepository.save(existingProductDetail);
    }

    // Delete method for ProductDetailService
    public void deleteByProductId(Long id) {
        ProductDetail productDetail = getProductDetailByProductId(id);
        productDetailRepository.delete(productDetail);
    }
}
