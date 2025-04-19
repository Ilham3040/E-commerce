package com.example.shoppingapi.service;

import com.example.shoppingapi.model.StoreCategoryItem;
import com.example.shoppingapi.model.StoreCategoryItemId;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.StoreCategoryItemRepository;
import com.example.shoppingapi.repository.StoreCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StoreCategoryItemService {

    private final StoreCategoryItemRepository storeCategoryItemRepository;
    private final StoreCategoryRepository     storeCategoryRepository;
    private final ProductRepository           productRepository;

    public List<StoreCategoryItem> findAll() {
        return storeCategoryItemRepository.findAll();
    }

    public StoreCategoryItem findById(StoreCategoryItemId id) {
        return storeCategoryItemRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("StoreCategoryItem not found with ID: " + id));
    }

    public StoreCategoryItem saveStoreCategoryItem(StoreCategoryItem item) {
        storeCategoryRepository.findById(item.getStoreCategory().getCategoryId())
            .orElseThrow(() ->
                new ResourceNotFoundException("Category not found with ID: " + item.getStoreCategory().getCategoryId()));
        productRepository.findById(item.getProduct().getProductId())
            .orElseThrow(() ->
                new ResourceNotFoundException("Product not found with ID: " + item.getProduct().getProductId()));
        return storeCategoryItemRepository.save(item);
    }

    public StoreCategoryItem updateStoreCategoryItem(StoreCategoryItemId id,
                                                    StoreCategoryItem item) {
        findById(id);  // 404 if missing
        storeCategoryRepository.findById(item.getStoreCategory().getCategoryId())
            .orElseThrow(() ->
                new ResourceNotFoundException("Category not found with ID: " + item.getStoreCategory().getCategoryId()));
        productRepository.findById(item.getProduct().getProductId())
            .orElseThrow(() ->
                new ResourceNotFoundException("Product not found with ID: " + item.getProduct().getProductId()));
        item.setId(id);
        return storeCategoryItemRepository.save(item);
    }

    public StoreCategoryItem partialUpdateStoreCategoryItem(StoreCategoryItemId id,
                                                            Map<String, Object> updates) {
        StoreCategoryItem existing = findById(id);
        BeanWrapper wrapper = new BeanWrapperImpl(existing);
        updates.forEach(wrapper::setPropertyValue);
        return storeCategoryItemRepository.save(existing);
    }

    public StoreCategoryItem softDeleteStoreCategoryItem(StoreCategoryItemId id) {
        StoreCategoryItem existing = findById(id);
        existing.setDeletedAt(LocalDateTime.now());
        return storeCategoryItemRepository.save(existing);
    }
}
