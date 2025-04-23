package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.patch.StoreCategoryPatchDTO;
import com.example.shoppingapi.dto.put.StoreCategoryPutDTO;
import com.example.shoppingapi.model.*;
import com.example.shoppingapi.repository.StoreCategoryRepository;
import com.example.shoppingapi.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreCategoryService {

    private final StoreCategoryRepository storeCategoryRepository;
    private final StoreRepository         storeRepository;

    public List<StoreCategory> findAll() {
        return storeCategoryRepository.findAll();
    }

    public StoreCategory getStoreCategoryById(Long id) {
        return storeCategoryRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("StoreCategory not found with ID: " + id));
    }

    public StoreCategory saveStoreCategory(StoreCategory category) {
        Long storeId = Optional.ofNullable(category.getStore())
            .map(Store::getStoreId)
            .orElseThrow(() ->
                new IllegalArgumentException("Store ID is required to create a store category."));
        storeRepository.findById(storeId)
            .orElseThrow(() ->
                new ResourceNotFoundException("Store not found with ID: " + storeId));
        return storeCategoryRepository.save(category);
    }

    public StoreCategory updateStoreCategory(Long id, StoreCategoryPutDTO storeCategoryPutDTO) {
        StoreCategory existingStoreCategory = getStoreCategoryById(id);
        ReflectionUtils.doWithFields(StoreCategoryPutDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(storeCategoryPutDTO);
            if (value != null) {
                Field storeCategoryField = ReflectionUtils.findField(StoreCategory.class, field.getName());
                if (storeCategoryField != null) {
                    storeCategoryField.setAccessible(true);
                    storeCategoryField.set(existingStoreCategory, value);
                }
            }
        });
        return storeCategoryRepository.save(existingStoreCategory);
    }

    public StoreCategory partiallyUpdateStoreCategory(Long id, StoreCategoryPatchDTO storeCategoryPatchDTO) {
        StoreCategory existingStoreCategory = getStoreCategoryById(id);
        ReflectionUtils.doWithFields(StoreCategoryPatchDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(storeCategoryPatchDTO);
            if (value != null) {
                Field storeCategoryField = ReflectionUtils.findField(StoreCategory.class, field.getName());
                if (storeCategoryField != null) {
                    storeCategoryField.setAccessible(true);
                    storeCategoryField.set(existingStoreCategory, value);
                }
            }
        });
        return storeCategoryRepository.save(existingStoreCategory);
    }

    // Delete method for StoreCategoryService
    public void deleteStoreCategoryById(Long id) {
        StoreCategory storeCategory = getStoreCategoryById(id);
        storeCategoryRepository.delete(storeCategory);
    }
}
