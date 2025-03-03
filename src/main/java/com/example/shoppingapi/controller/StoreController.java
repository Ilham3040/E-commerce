package com.example.shoppingapi.controller;

import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.shoppingapi.dto.StoreDTO;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    // Get all stores
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
    public ResponseEntity<StoreDTO> createOrUpdateStore(@RequestBody Store store) {
        Store savedStore = storeService.saveStore(store);
        
        // Convert to DTO before returning
        StoreDTO storeDTO = new StoreDTO(savedStore.getStoreId(), savedStore.getStoreName());

        return ResponseEntity.ok(storeDTO);
    }

    // Delete a store by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }
}
