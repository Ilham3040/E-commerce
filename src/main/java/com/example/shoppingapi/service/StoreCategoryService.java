package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.StoreCategory;
import com.example.shoppingapi.repository.StoreCategoryRepository;
import com.example.shoppingapi.repository.StoreRepository;
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
public class StoreCategoryService {

    @Autowired
    private  StoreRepository storeRepository;

    @Autowired
    private StoreCategoryRepository storeCategoryRepository;

    public List<StoreCategory> findAll() {
        return storeCategoryRepository.findAll();
    }

    public Optional<StoreCategory> findById(Long id) {
        return storeCategoryRepository.findById(id);
    }

    public StoreCategory saveStoreCategory(StoreCategory storeCategory) {
        Optional<Store> existingOpt = storeRepository.findById(storeCategory.getStore().getStoreId());
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("Store not found with ID: " + storeCategory.getStore().getStoreId());
        }
        return storeCategoryRepository.save(storeCategory);
    }

    public StoreCategory updateStoreCategory(Long id, StoreCategory storeCategory) {
        Optional<StoreCategory> existingCategory = storeCategoryRepository.findById(id);
        if (existingCategory.isEmpty()) {
            throw new IllegalArgumentException("StoreCategory not found with ID: " + id);
        }
        Optional<Store> existingStore = storeRepository.findById(storeCategory.getStore().getStoreId());
        if (existingStore.isEmpty()) {
            throw new IllegalArgumentException("Store not found with ID: " + id);
        }
        storeCategory.setCategoryId(id);
        return storeCategoryRepository.save(storeCategory);
    }

    public StoreCategory partialUpdateStoreCategory(Long id, Map<String, Object> updates) {
        Optional<StoreCategory> existingOpt = storeCategoryRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("StoreCategory not found with ID: " + id);
        }
        StoreCategory storeCategory = existingOpt.get();
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(StoreCategory.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, storeCategory, value);
            }
        });
        return storeCategoryRepository.save(storeCategory);
    }

    public StoreCategory deleteById(Long id) {
        StoreCategory storeCategory = storeCategoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("StoreCategory not found with ID: " + id));

        storeCategory.setDeletedAt(LocalDateTime.now());
        storeCategoryRepository.save(storeCategory);
        return storeCategory;
    }
}
