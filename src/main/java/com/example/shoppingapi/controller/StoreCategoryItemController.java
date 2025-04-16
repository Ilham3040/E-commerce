package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.dto.response.StoreCategoryItemDTO;
import com.example.shoppingapi.model.StoreCategoryItem;
import com.example.shoppingapi.service.StoreCategoryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/store-category-items")
public class StoreCategoryItemController {

    @Autowired
    private StoreCategoryItemService storeCategoryItemService;

    @GetMapping
    public List<StoreCategoryItem> getAllStoreCategoryItems() {
        return storeCategoryItemService.findAll();
    }

    @GetMapping("/{categoryId}/{productId}")
    public Optional<StoreCategoryItem> getStoreCategoryItemById(@PathVariable Long categoryId,
                                                                @PathVariable Long productId) {
        return storeCategoryItemService.findById(categoryId, productId);
    }

    @PostMapping
    public ResponseEntity<StoreCategoryItemDTO> createStoreCategoryItem(@RequestBody StoreCategoryItem item) {
        StoreCategoryItem savedItem = storeCategoryItemService.saveStoreCategoryItem(item);
        StoreCategoryItemDTO dto = new StoreCategoryItemDTO(savedItem.getId().getCategoryId(),
                savedItem.getId().getProductId());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{categoryId}/{productId}")
    public ResponseEntity<ApiResponse<StoreCategoryItemDTO>> updateStoreCategoryItem(
            @PathVariable Long categoryId,
            @PathVariable Long productId,
            @RequestBody StoreCategoryItem item) {
        try {
            StoreCategoryItem updatedItem = storeCategoryItemService.updateStoreCategoryItem(categoryId, productId, item);
            StoreCategoryItemDTO dto = new StoreCategoryItemDTO(updatedItem.getId().getCategoryId(),
                    updatedItem.getId().getProductId());
            ApiResponse<StoreCategoryItemDTO> response = new ApiResponse<>("StoreCategoryItem successfully updated", dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<StoreCategoryItemDTO> response = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PatchMapping("/{categoryId}/{productId}")
    public ResponseEntity<ApiResponse<StoreCategoryItemDTO>> partialUpdateStoreCategoryItem(
            @PathVariable Long categoryId,
            @PathVariable Long productId,
            @RequestBody Map<String, Object> updates) {
        try {
            StoreCategoryItem updatedItem = storeCategoryItemService.partialUpdateStoreCategoryItem(categoryId, productId, updates);
            StoreCategoryItemDTO dto = new StoreCategoryItemDTO(updatedItem.getId().getCategoryId(),
                    updatedItem.getId().getProductId());
            ApiResponse<StoreCategoryItemDTO> response = new ApiResponse<>("StoreCategoryItem successfully updated", dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<StoreCategoryItemDTO> response = new ApiResponse<>("Error updating StoreCategoryItem: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{categoryId}/{productId}")
    public void deleteByIdCategoryItem(@PathVariable Long categoryId,
                                        @PathVariable Long productId) {
        storeCategoryItemService.deleteById(categoryId, productId);
    }
}
