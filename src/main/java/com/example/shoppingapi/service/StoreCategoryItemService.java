package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.StoreCategoryItemCreateDTO;
import com.example.shoppingapi.model.*;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.StoreCategoryItemRepository;
import com.example.shoppingapi.repository.StoreCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StoreCategoryItemService {

    private final StoreCategoryItemRepository storeCategoryItemRepository;
    private final StoreCategoryRepository     storeCategoryRepository;
    private final ProductRepository           productRepository;

    public List<StoreCategoryItem> getAllItems() {
        return storeCategoryItemRepository.findAll();
    }

    public StoreCategoryItem getStoreCategoryItemByID(StoreCategoryItemId id) {
        return storeCategoryItemRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Category Item not found with ID: " + id));
    }

    public StoreCategoryItem saveStoreCategoryItem(StoreCategoryItemCreateDTO storeCategoryItemCreateDTO) {
        storeCategoryRepository.findById(storeCategoryItemCreateDTO.getCategoryId())
            .orElseThrow(() ->
                new ResourceNotFoundException("Category not found with ID: " + storeCategoryItemCreateDTO.getCategoryId()));
        productRepository.findById(storeCategoryItemCreateDTO.getProductId())
            .orElseThrow(() ->
                new ResourceNotFoundException("Product not found with ID: " + storeCategoryItemCreateDTO.getProductId()));

        StoreCategoryItemId storeCategoryItemId = StoreCategoryItemId.builder()
                .categoryId(storeCategoryItemCreateDTO.getCategoryId())
                .productId(storeCategoryItemCreateDTO.getProductId())
                .build();

        StoreCategoryItem newItemCategory = StoreCategoryItem.builder()
                .storeCategory(StoreCategory.builder().categoryId(storeCategoryItemCreateDTO.getCategoryId()).build())
                .product(Product.builder().productId(storeCategoryItemCreateDTO.getProductId()).build())
                .build();

        return storeCategoryItemRepository.save(newItemCategory);

    }

    public void deleteItemFromCategory(StoreCategoryItemId id) {
        StoreCategoryItem selectedItemCategory = getStoreCategoryItemByID(id);

        storeCategoryItemRepository.delete(selectedItemCategory);
    }




}
