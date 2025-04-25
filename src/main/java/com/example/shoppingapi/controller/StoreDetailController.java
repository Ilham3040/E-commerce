//package com.example.shoppingapi.controller;
//
//import com.example.shoppingapi.dto.request.StoreDetailCreateDTO;
//import com.example.shoppingapi.dto.response.StoreDetailDTO;
//import com.example.shoppingapi.model.*;
//import com.example.shoppingapi.service.StoreDetailService;
//import com.example.shoppingapi.dto.response.ApiResponse;
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
//@RestController
//@RequestMapping("/api/storesdetail")
//@RequiredArgsConstructor
//public class StoreDetailController {
//    private final StoreDetailService storeDetailService;
//
//    @GetMapping
//    public ApiResponse<List<StoreDetailDTO>> getAllStoreDetail(){
//        List<StoreDetailDTO> datas = storeDetailService.findAll()
//            .stream()
//            .map(detail -> new StoreDetailDTO(detail.getStoreDetailId(), detail.getStore().getStoreId()))
//            .collect(Collectors.toList());
//        return new ApiResponse<>("Successfully fetched all StoreDetail", datas,HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ApiResponse<StoreDetail> getDetailById(@PathVariable Long id){
//        StoreDetail data = storeDetailService.findById(id);
//        return new ApiResponse<>("Successfully fetch data", data,HttpStatus.OK);
//    }
//
//    @PostMapping
//    public ApiResponse<StoreDetailDTO> createStoreDetail(@Validated @RequestBody StoreDetailCreateDTO dto){
//        StoreDetail wannabe = StoreDetail.builder()
//            .store(Store.builder().storeId(dto.getStoreId()).build())
//            .address(dto.getAddress())
//            .description(dto.getDescription())
//            .build();
//
//        StoreDetail created = storeDetailService.saveStoreDetail(wannabe);
//        return new ApiResponse<>("Successfully creating store detail",
//            new StoreDetailDTO(created.getStoreDetailId(), created.getStore().getStoreId()),HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{id}")
//    public ApiResponse<StoreDetailDTO> updateEntireStoreDetail(@PathVariable Long id, @Validated @RequestBody StoreDetailCreateDTO dto)
//    {
//        StoreDetail wannabe = StoreDetail.builder()
//            .storeDetailId(id)
//            .store(Store.builder().storeId(dto.getStoreId()).build())
//            .address(dto.getAddress())
//            .description(dto.getDescription())
//            .build();
//
//        StoreDetail updated = storeDetailService.updateStoreDetail(id, wannabe);
//        return new ApiResponse<>("Successfully creating store detail",
//            new StoreDetailDTO(updated.getStoreDetailId(), updated.getStore().getStoreId()),HttpStatus.OK);
//    }
//
//    @PatchMapping("/{id}")
//    public ApiResponse<StoreDetailDTO> updatePartiallyDetail(@PathVariable Long id, @RequestBody Map<String,Object> updates)
//    {
//        StoreDetail updated = storeDetailService.partialUpdateStoreDetail(id, updates);
//        return new ApiResponse<>("Successfully creating store detail",
//            new StoreDetailDTO(updated.getStoreDetailId(), updated.getStore().getStoreId()),HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    public ApiResponse<StoreDetail> deleteStoreDetail(@PathVariable Long id){
//        storeDetailService.deleteById(id);
//        return new ApiResponse<>("Successfully deleted detail with ID :" + id, null,HttpStatus.NO_CONTENT);
//    }
//
//}
