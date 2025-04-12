package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.ApiResponse;
import com.example.shoppingapi.dto.StoreCategoryDTO;
import com.example.shoppingapi.model.StoreCategory;
import com.example.shoppingapi.service.StoreCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/store-categories")
public class StoreCategoryController {

    @Autowired
    private StoreCategoryService storeCategoryService;

    @GetMapping
    public List<StoreCategory> getAllStoreCategories() {
        return storeCategoryService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<StoreCategory> getStoreCategoryById(@PathVariable Long id) {
        return storeCategoryService.findById(id);
    }

    @PostMapping
    public ResponseEntity<StoreCategoryDTO> createStoreCategory(@RequestBody StoreCategory storeCategory) {
        StoreCategory savedStoreCategory = storeCategoryService.saveStoreCategory(storeCategory);
        StoreCategoryDTO dto = new StoreCategoryDTO(savedStoreCategory.getCategoryId(),
                savedStoreCategory.getStore().getStoreId());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StoreCategoryDTO>> updateStoreCategory(@PathVariable Long id,
                                                                             @RequestBody StoreCategory storeCategory) {
        try {
            StoreCategory updatedStoreCategory = storeCategoryService.updateStoreCategory(id, storeCategory);
            StoreCategoryDTO dto = new StoreCategoryDTO(updatedStoreCategory.getCategoryId(),
                    updatedStoreCategory.getStore().getStoreId());
            ApiResponse<StoreCategoryDTO> response = new ApiResponse<>("StoreCategory successfully updated", dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<StoreCategoryDTO> response = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<StoreCategoryDTO>> partialUpdateStoreCategory(@PathVariable Long id,
                                                                                    @RequestBody Map<String, Object> updates) {
        try {
            StoreCategory updatedStoreCategory = storeCategoryService.partialUpdateStoreCategory(id, updates);
            StoreCategoryDTO dto = new StoreCategoryDTO(updatedStoreCategory.getCategoryId(),
                    updatedStoreCategory.getStore().getStoreId());
            ApiResponse<StoreCategoryDTO> response = new ApiResponse<>("StoreCategory successfully updated", dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<StoreCategoryDTO> response = new ApiResponse<>("Error updating StoreCategory: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteStoreCategory(@PathVariable Long id) {
        storeCategoryService.deleteById(id);
    }
}
