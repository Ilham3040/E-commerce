package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        if (product.getStore() == null || product.getStore().getStoreId() == null) {
            throw new IllegalArgumentException("Store ID is required to create a product.");
        }

        Optional<Store> store = storeRepository.findById(product.getStore().getStoreId());
        if (store.isEmpty()) {
            throw new IllegalArgumentException("Store not found. Cannot create product.");
        }

        return productRepository.save(product);
    }

    public Product updateProduct(Long id,Product product) {
        Optional<Product> existingProductOpt = productRepository.findById(id);
        if (existingProductOpt.isEmpty()) {
            throw new IllegalArgumentException("Product not found with ID: " + id);
        }

        if (product.getStore() == null || product.getStore().getStoreId() == null) {
            throw new IllegalArgumentException("Store ID is required to create a product.");
        }

        Optional<Store> store = storeRepository.findById(product.getStore().getStoreId());
        if (store.isEmpty()) {
            throw new IllegalArgumentException("Store not found. Cannot create product.");
        }

        Product updatedProduct = product;
        updatedProduct.setProductId(id);

        return productRepository.save(updatedProduct);
    }

public Product partialUpdateProduct(Long id, Map<String, Object> updates) {
    Optional<Product> existingProductOpt = productRepository.findById(id);
    if (existingProductOpt.isEmpty()) {
        throw new IllegalArgumentException("Product not found with ID: " + id);
    }
    
    Product existingProduct = existingProductOpt.get();
    updates.forEach((key, value) -> {
        Field field = ReflectionUtils.findField(Product.class, key);
        if (field != null) {
            field.setAccessible(true);
            if ("price".equals(key) && value instanceof Number) {
                // Convert Number to BigDecimal
                BigDecimal priceValue = new BigDecimal(value.toString());
                ReflectionUtils.setField(field, existingProduct, priceValue);
            } else {
                ReflectionUtils.setField(field, existingProduct, value);
            }
        }
    });

    return productRepository.save(existingProduct);
}


    public List<Product> getProductsByStoreId(Long storeId) {
        return productRepository.findByStoreStoreId(storeId);
    }


    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
