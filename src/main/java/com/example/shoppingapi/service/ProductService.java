package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.ProductCreateDTO;
import com.example.shoppingapi.dto.patch.ProductPatchDTO;
import com.example.shoppingapi.dto.put.ProductPutDTO;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.StoreRepository;

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
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Product not found with ID: " + id));
    }

    public Product saveProduct(ProductCreateDTO dto) {
        storeRepository.findById(dto.getStoreId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Store not found with ID: " + dto.getStoreId()));

        Product saved = Product.builder()
                .productName(dto.getProductName())
                .store(Store.builder().storeId(dto.getStoreId()).build())
                .build();

        return productRepository.save(saved);
    }

    public Product updateProduct(Long id, ProductPutDTO productPutDTO) {
        Product existingProduct = getProductById(id);
        ReflectionUtils.doWithFields(ProductPutDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(productPutDTO);
            if (value != null) {
                Field productField = ReflectionUtils.findField(Product.class, field.getName());
                if (productField != null) {
                    productField.setAccessible(true);
                    productField.set(existingProduct, value);
                }
            }
        });
        return productRepository.save(existingProduct);
    }

    public Product partialUpdateProduct(Long id, ProductPatchDTO productPatchDTO) {
        Product existingProduct = getProductById(id);
        ReflectionUtils.doWithFields(ProductPatchDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(productPatchDTO);
            if (value != null) {
                Field productField = ReflectionUtils.findField(Product.class, field.getName());
                if (productField != null) {
                    productField.setAccessible(true);
                    productField.set(existingProduct, value);
                }
            }
        });
        return productRepository.save(existingProduct);
    }

    public void deleteById(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

}
