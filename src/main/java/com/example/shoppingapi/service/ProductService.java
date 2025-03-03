package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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

    // Get products by store ID
    public List<Product> getProductsByStoreId(Long storeId) {
        return productRepository.findByStoreStoreId(storeId);
    }


    // Delete a product by ID
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
