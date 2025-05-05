package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.create.StoreCategoryCreateDTO;
import com.example.shoppingapi.dto.detailed.DetailedStoreCategoryDTO;
import com.example.shoppingapi.dto.put.StoreCategoryPutDTO;
import com.example.shoppingapi.dto.patch.StoreCategoryPatchDTO;
import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.dto.response.StoreCategoryDTO;
import com.example.shoppingapi.model.StoreCategory;
import com.example.shoppingapi.service.StoreCategoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/storecategories/")
@RequiredArgsConstructor
public class StoreCategoryController {
    private final StoreCategoryService storeCategoryService;

    @GetMapping
    public ApiResponse<List<StoreCategoryDTO>> getAllStoreCategories() {
        List<StoreCategoryDTO> storeCategoryDTOs = storeCategoryService.findAll()
                .stream()
                .map(category -> new StoreCategoryDTO(category.getCategoryId(), category.getStore().getStoreId()))
                .collect(Collectors.toList());
        return new ApiResponse<>("Successfully fetched all store categories", storeCategoryDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ApiResponse<DetailedStoreCategoryDTO> getStoreCategoryById(@PathVariable Long id) {
        StoreCategory storeCategory = storeCategoryService.getStoreCategoryById(id);
        return new ApiResponse<>("Successfully fetched store category", DetailedStoreCategoryDTO.builder()
                .categoryId(storeCategory.getCategoryId())
                .categoryName(storeCategory.getCategoryName())
                .storeId(storeCategory.getStore().getStoreId())
                .createdAt(storeCategory.getCreatedAt())
                .updatedAt(storeCategory.getUpdatedAt())
                .build(), HttpStatus.OK);
    }

    @GetMapping("/store/{storeId}")
    public ApiResponse<List<StoreCategoryDTO>> getAllCategoriesByStoreId(@PathVariable Long storeId) {
        List<StoreCategory> storeCategories = storeCategoryService.getAllCategoriesByStoreId(storeId);
        List<StoreCategoryDTO> storeCategoryDTOs = storeCategories.stream()
                .map(category -> new StoreCategoryDTO(category.getCategoryId(), category.getStore().getStoreId()))
                .collect(Collectors.toList());

        return new ApiResponse<>("Successfully fetched store categories by store ID", storeCategoryDTOs, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<StoreCategoryDTO> createStoreCategory(@Validated @RequestBody StoreCategoryCreateDTO storeCategoryCreateDTO) {
        StoreCategory createdStoreCategory = storeCategoryService.saveStoreCategory(storeCategoryCreateDTO);
        return new ApiResponse<>("Successfully created store category", new StoreCategoryDTO(createdStoreCategory.getCategoryId(), createdStoreCategory.getStore().getStoreId()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ApiResponse<StoreCategoryDTO> updateStoreCategory(@PathVariable Long id, @Validated @RequestBody StoreCategoryPutDTO storeCategoryPutDTO) {
        StoreCategory updatedStoreCategory = storeCategoryService.updateStoreCategory(id, storeCategoryPutDTO);
        return new ApiResponse<>("Successfully updated store category", new StoreCategoryDTO(updatedStoreCategory.getCategoryId(), updatedStoreCategory.getStore().getStoreId()), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ApiResponse<StoreCategoryDTO> partialUpdateStoreCategory(@PathVariable Long id, @RequestBody StoreCategoryPatchDTO storeCategoryPatchDTO) {
        StoreCategory updatedStoreCategory = storeCategoryService.partiallyUpdateStoreCategory(id, storeCategoryPatchDTO);
        return new ApiResponse<>("Successfully partially updated store category", new StoreCategoryDTO(updatedStoreCategory.getCategoryId(), updatedStoreCategory.getStore().getStoreId()), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteStoreCategory(@PathVariable Long id) {
        storeCategoryService.deleteById(id);
        return new ApiResponse<>("Successfully deleted store category", null, HttpStatus.NO_CONTENT);
    }
}
