package com.example.shoppingapi.service;

import com.example.shoppingapi.model.StoreCategory;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.repository.StoreCategoryRepository;
import com.example.shoppingapi.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public StoreCategory findById(Long id) {
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

    public StoreCategory updateStoreCategory(Long id, StoreCategory category) {
        if (!id.equals(category.getCategoryId())) {
            throw new IllegalArgumentException("Category ID in URL and body must match.");
        }
        findById(id);
        Long storeId = Optional.ofNullable(category.getStore())
            .map(Store::getStoreId)
            .orElseThrow(() ->
                new IllegalArgumentException("Store ID is required to update a store category."));
        storeRepository.findById(storeId)
            .orElseThrow(() ->
                new ResourceNotFoundException("Store not found with ID: " + storeId));
        category.setCategoryId(id);
        return storeCategoryRepository.save(category);
    }

    public StoreCategory partialUpdateStoreCategory(Long id, Map<String, Object> updates) {
        StoreCategory existing = findById(id);
        BeanWrapper wrapper = new BeanWrapperImpl(existing);
        updates.forEach(wrapper::setPropertyValue);
        return storeCategoryRepository.save(existing);
    }

    public StoreCategory softDeleteStoreCategory(Long id) {
        StoreCategory existing = findById(id);
        existing.setDeletedAt(LocalDateTime.now());
        return storeCategoryRepository.save(existing);
    }
}
