//package com.example.shoppingapi.controller;
//
//import com.example.shoppingapi.dto.request.StoreCreateDTO;
//import com.example.shoppingapi.dto.response.ApiResponse;
//import com.example.shoppingapi.dto.response.StoreDTO;
//import com.example.shoppingapi.model.Store;
//import com.example.shoppingapi.model.User;
//import com.example.shoppingapi.service.StoreService;
//
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//
//@RestController
//@RequestMapping("/api/stores")
//@RequiredArgsConstructor
//public class StoreController {
//    private final StoreService storeService;
//
//    @GetMapping
//    public ApiResponse<List<StoreDTO>> getAllStores() {
//        List<StoreDTO> dtos = storeService.getAllStores()
//            .stream()
//            .map(s -> new StoreDTO(s.getStoreId(), s.getUser().getUserId()))
//            .collect(Collectors.toList());
//        return new ApiResponse<>("Fetched all stores", dtos,HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ApiResponse<StoreDTO> getStoreById(@PathVariable Long id) {
//        Store store = storeService.getStoreById(id);
//        return new ApiResponse<>("Fetched store", new StoreDTO(store.getStoreId(), store.getUser().getUserId()), HttpStatus.OK);
//    }
//
//    @PostMapping
//    public ApiResponse<StoreDTO> createStore(@Validated @RequestBody StoreCreateDTO dto) {
//        Store toCreate = Store.builder()
//            .storeName(dto.getStoreName())
//            .user(User.builder().userId(dto.getUserId()).build())
//            .build();
//        Store created = storeService.saveStore(toCreate);
//        return new ApiResponse<>("Store created", new StoreDTO(created.getStoreId(), created.getUser().getUserId()),HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{id}")
//    public ApiResponse<StoreDTO> updateStore(
//        @PathVariable Long id,
//        @Validated @RequestBody StoreCreateDTO dto
//    ) {
//        Store toUpdate = Store.builder()
//            .storeId(id)
//            .storeName(dto.getStoreName())
//            .user(User.builder().userId(dto.getUserId()).build())
//            .build();
//        Store updated = storeService.updateStore(id, toUpdate);
//        return new ApiResponse<>("Store updated", new StoreDTO(updated.getStoreId(), updated.getUser().getUserId()),HttpStatus.OK);
//    }
//
//    @PatchMapping("/{id}")
//    public ApiResponse<StoreDTO> partialUpdateStore(
//        @PathVariable Long id,
//        @RequestBody Map<String,Object> updates
//    ) {
//        Store updated = storeService.partialUpdateStore(id, updates);
//        return new ApiResponse<>("Store partially updated", new StoreDTO(updated.getStoreId(), updated.getUser().getUserId()),HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    public ApiResponse<Void> deleteStore(@PathVariable Long id) {
//        storeService.deleteById(id);
//        return new ApiResponse<>("Store deleted", null,HttpStatus.NO_CONTENT);
//    }
//}
