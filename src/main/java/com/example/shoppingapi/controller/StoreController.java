package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.create.StoreCreateDTO;
import com.example.shoppingapi.dto.patch.StorePatchDTO;
import com.example.shoppingapi.dto.put.StorePutDTO;
import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.dto.response.StoreDTO;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.service.StoreService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @GetMapping
    public ApiResponse<List<StoreDTO>> getAllStores() {
        List<StoreDTO> storeDTOs = storeService.getAllStores()
                .stream()
                .map(store -> new StoreDTO(store.getStoreId(), store.getUser().getUserId()))
                .collect(Collectors.toList());
        return new ApiResponse<>("Fetched all stores", storeDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ApiResponse<StoreDTO> getStoreById(@PathVariable Long id) {
        Store store = storeService.getStoreById(id);
        return new ApiResponse<>("Fetched store", new StoreDTO(store.getStoreId(), store.getUser().getUserId()), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<StoreDTO> createStore(@Validated @RequestBody StoreCreateDTO storeCreateDTO) {
        Store createdStore = storeService.saveStore(storeCreateDTO);
        return new ApiResponse<>("Store created", new StoreDTO(createdStore.getStoreId(), createdStore.getUser().getUserId()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ApiResponse<StoreDTO> updateStore(@PathVariable Long id, @Validated @RequestBody StorePutDTO storePutDTO) {
        Store updatedStore = storeService.updateStore(id, storePutDTO);
        return new ApiResponse<>("Store updated", new StoreDTO(updatedStore.getStoreId(), updatedStore.getUser().getUserId()), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ApiResponse<StoreDTO> partialUpdateStore(@PathVariable Long id, @Validated @RequestBody StorePatchDTO storePatchDTO) {
        Store updatedStore = storeService.partialUpdateStore(id, storePatchDTO);
        return new ApiResponse<>("Store partially updated", new StoreDTO(updatedStore.getStoreId(), updatedStore.getUser().getUserId()), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteStore(@PathVariable Long id) {
        storeService.deleteById(id);
        return new ApiResponse<>("Store deleted", null, HttpStatus.NO_CONTENT);
    }
}
