package com.example.shoppingapi.controller;

import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.shoppingapi.dto.ApiResponse;
import com.example.shoppingapi.dto.StoreDTO;

import java.util.List;
import java.util.Optional;
import java.util.Map;


@RestController
@RequestMapping("/api/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;


    @GetMapping
    public List<Store> getAllStores() {
        return storeService.getAllStores();
    }

    // Get store by ID
    @GetMapping("/{id}")
    public ResponseEntity<Store> getStoreById(@PathVariable Long id) {
        Optional<Store> store = storeService.getStoreById(id);
        return store.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StoreDTO>> createStore(@RequestBody Store store) {
        Store savedStore = storeService.saveStore(store);
        
        StoreDTO storeDTO = new StoreDTO(savedStore.getStoreId(),savedStore.getUser().getUserId());
        ApiResponse<StoreDTO> response = new ApiResponse<>("Store successfully added", storeDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StoreDTO>> updateStore(
            @PathVariable Long id,
            @RequestBody Store store) {
        try {
            Store updatedStore = storeService.updateStore(id, store);
            StoreDTO storeDTO = new StoreDTO(updatedStore.getStoreId(), updatedStore.getUser().getUserId());
            ApiResponse<StoreDTO> response = new ApiResponse<>("Store successfully updated", storeDTO);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<StoreDTO> response = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Partial Update (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<StoreDTO>> partialUpdateStore(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        try {
            Store updatedStore = storeService.partialUpdateStore(id, updates);
            StoreDTO storeDTO = new StoreDTO(updatedStore.getStoreId(), updatedStore.getUser().getUserId());
            ApiResponse<StoreDTO> response = new ApiResponse<>("Store successfully updated", storeDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<StoreDTO> response = new ApiResponse<>("Error updating store: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    



    // Delete a store by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }
}
