package com.example.shoppingapi.service;

import com.example.shoppingapi.model.StoreCategoryItem;
import com.example.shoppingapi.model.StoreCategoryItemId;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.StoreCategoryItemRepository;
import com.example.shoppingapi.repository.StoreCategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StoreCategoryItemService {

    @Autowired
    private StoreCategoryItemRepository storeCategoryItemRepository;

    @Autowired
    private StoreCategoryRepository storeCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<StoreCategoryItem> findAll() {
        return storeCategoryItemRepository.findAll();
    }

    public Optional<StoreCategoryItem> findById(StoreCategoryItemId id) {
        return storeCategoryItemRepository.findById(id);
    }

    public StoreCategoryItem saveStoreCategoryItem(StoreCategoryItem item) {
        storeCategoryRepository.findById(item.getStoreCategory().getCategoryId())
        .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        productRepository.findById(item.getProduct().getProductId())
        .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return storeCategoryItemRepository.save(item);
    }

    public StoreCategoryItem updateStoreCategoryItem(StoreCategoryItemId id, StoreCategoryItem item) {
        storeCategoryItemRepository.findById(id).
        orElseThrow(() -> new ResourceNotFoundException("Category Item not found"));

        storeCategoryRepository.findById(item.getStoreCategory().getCategoryId())
        .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        productRepository.findById(item.getProduct().getProductId())
        .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        item.setId(id);
        return storeCategoryItemRepository.save(item);
    }

    public StoreCategoryItem partialUpdateStoreCategoryItem(StoreCategoryItemId id, Map<String, Object> updates) {

        Optional<StoreCategoryItem> existingOpt = storeCategoryItemRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new ResourceNotFoundException("Category Item not found");
        }
        StoreCategoryItem item = existingOpt.get();
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(StoreCategoryItem.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, item, value);
            }
        });
        return storeCategoryItemRepository.save(item);
    }

    public StoreCategoryItem deleteById(StoreCategoryItemId id) {
        StoreCategoryItem storeCategoryItem = storeCategoryItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Item on this category not found with ID: " + id));

        storeCategoryItem.setDeletedAt(LocalDateTime.now());
        storeCategoryItemRepository.save(storeCategoryItem);
        return storeCategoryItem;
    }
}
