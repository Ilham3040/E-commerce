//package com.example.shoppingapi.controller;
//
//import com.example.shoppingapi.dto.request.StoreCategoryRequestDTO;
//import com.example.shoppingapi.dto.response.StoreCategoryDTO;
//import com.example.shoppingapi.model.*;
//import com.example.shoppingapi.dto.response.ApiResponse;
//
//import com.example.shoppingapi.service.StoreCategoryService;
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
//@RestController
//@RequestMapping("/api/storecategories")
//@RequiredArgsConstructor
//public class StoreCategoryController {
//    private final StoreCategoryService storeCategoryService;
//    @GetMapping
//    public ApiResponse<List<StoreCategoryDTO>> getAllStoreCategory(){
//        List<StoreCategoryDTO> data = storeCategoryService.findAll()
//                .stream()
//                .map(category -> new StoreCategoryDTO(category.getCategoryId(), category.getStore().getStoreId()))
//                .collect(Collectors.toList());
//        return new ApiResponse<>("Successfully fetched all StoreCategory", data,HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ApiResponse<StoreCategory> getDetailById(@PathVariable Long id){
//        StoreCategory data = storeCategoryService.findById(id);
//        return new ApiResponse<>("Successfully fetch data", data,HttpStatus.OK);
//    }
//
//    @PostMapping
//    public ApiResponse<StoreCategoryDTO> createStoreCategory(@Validated @RequestBody StoreCategoryRequestDTO dto){
//        StoreCategory wannabe = StoreCategory.builder()
//                .store(Store.builder().storeId(dto.getStoreId()).build())
//                .build();
//
//        StoreCategory created = storeCategoryService.saveStoreCategory(wannabe);
//        return new ApiResponse<>("Successfully creating store category",
//                new StoreCategoryDTO(created.getCategoryId(), created.getStore().getStoreId()),HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{id}")
//    public ApiResponse<StoreCategoryDTO> updateEntireStoreCategory(@PathVariable Long id, @Validated @RequestBody StoreCategoryRequestDTO dto)
//    {
//        StoreCategory wannabe = StoreCategory.builder()
//                .categoryId(id)
//                .store(Store.builder().storeId(dto.getStoreId()).build())
//                .build();
//
//        StoreCategory updated = storeCategoryService.updateStoreCategory(id, wannabe);
//        return new ApiResponse<>("Successfully creating store detail",
//                new StoreCategoryDTO(updated.getCategoryId(), updated.getStore().getStoreId()),HttpStatus.OK);
//    }
//
//    @PatchMapping("/{id}")
//    public ApiResponse<StoreCategoryDTO> updatePartiallyDetail(@PathVariable Long id, @RequestBody Map<String,Object> updates)
//    {
//        StoreCategory updated = storeCategoryService.partialUpdateStoreCategory(id, updates);
//        return new ApiResponse<>("Successfully creating store detail",
//                new StoreCategoryDTO(updated.getCategoryId(), updated.getStore().getStoreId()),HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    public ApiResponse<StoreCategory> deleteStoreCategory(@PathVariable Long id){
//        storeCategoryService.deleteById(id);
//        return new ApiResponse<>("Successfully deleted detail with ID :" + id, null,HttpStatus.NO_CONTENT);
//    }
//
//}
