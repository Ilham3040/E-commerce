package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

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

    public Product saveProduct(Product product) {
        Long storeId = Optional.ofNullable(product.getStore())
            .map(Store::getStoreId)
            .orElseThrow(() ->
                new IllegalArgumentException("Store ID is required to create a product."));

        storeRepository.findById(storeId)
            .orElseThrow(() ->
                new ResourceNotFoundException("Store not found with ID: " + storeId));

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        if (!id.equals(product.getProductId())) {
            throw new IllegalArgumentException("Product ID in URL and body must match.");
        }

        getProductById(id);

        Long storeId = Optional.ofNullable(product.getStore())
            .map(Store::getStoreId)
            .orElseThrow(() ->
                new IllegalArgumentException("Store ID is required to update a product."));

        storeRepository.findById(storeId)
            .orElseThrow(() ->
                new ResourceNotFoundException("Store not found with ID: " + storeId));

        product.setProductId(id);
        return productRepository.save(product);
    }

    public Product partialUpdateProduct(Long id, Map<String, Object> updates) {
        Product existing = getProductById(id);

        BeanWrapper wrapper = new BeanWrapperImpl(existing);
        updates.forEach((prop, val) -> {
            if ("price".equals(prop) && val instanceof Number) {
                wrapper.setPropertyValue(prop, BigDecimal.valueOf(((Number) val).longValue()));
            } else {
                wrapper.setPropertyValue(prop, val);
            }
        });

        return productRepository.save(existing);
    }

    public List<Product> getProductsByStoreId(Long storeId) {
        return productRepository.findByStoreStoreId(storeId);
    }

    public void deleteById(Long id) {
        Product product = productRepository.findById(id)
        .orElseThrow(() ->
            new ResourceNotFoundException("Product not found with ID: " + id));
        productRepository.delete(product);
    }
}
