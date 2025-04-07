package com.example.shoppingapi.service;

import com.example.shoppingapi.model.StoreCategoryItem;
import com.example.shoppingapi.model.StoreCategoryItemId;
import com.example.shoppingapi.repository.StoreCategoryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StoreCategoryItemService {

    @Autowired
    private StoreCategoryItemRepository storeCategoryItemRepository;

    public List<StoreCategoryItem> findAll() {
        return storeCategoryItemRepository.findAll();
    }

    public Optional<StoreCategoryItem> findById(Long categoryId, Long productId) {
        StoreCategoryItemId id = new StoreCategoryItemId();
        id.setCategoryId(categoryId);
        id.setProductId(productId);
        return storeCategoryItemRepository.findById(id);
    }

    public StoreCategoryItem saveStoreCategoryItem(StoreCategoryItem item) {
        return storeCategoryItemRepository.save(item);
    }

    public StoreCategoryItem updateStoreCategoryItem(Long categoryId, Long productId, StoreCategoryItem item) {
        StoreCategoryItemId id = new StoreCategoryItemId();
        id.setCategoryId(categoryId);
        id.setProductId(productId);
        Optional<StoreCategoryItem> existingOpt = storeCategoryItemRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("StoreCategoryItem not found with given composite key");
        }
        item.setId(id);
        return storeCategoryItemRepository.save(item);
    }

    public StoreCategoryItem partialUpdateStoreCategoryItem(Long categoryId, Long productId, Map<String, Object> updates) {
        StoreCategoryItemId id = new StoreCategoryItemId();
        id.setCategoryId(categoryId);
        id.setProductId(productId);
        Optional<StoreCategoryItem> existingOpt = storeCategoryItemRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("StoreCategoryItem not found with given composite key");
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

    public void deleteById(Long categoryId, Long productId) {
        StoreCategoryItemId id = new StoreCategoryItemId();
        id.setCategoryId(categoryId);
        id.setProductId(productId);
        storeCategoryItemRepository.deleteById(id);
    }
}
