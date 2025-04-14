package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.dto.response.StoreDetailDTO;
import com.example.shoppingapi.model.StoreDetail;
import com.example.shoppingapi.service.StoreDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/store-details")
public class StoreDetailController {

    @Autowired
    private StoreDetailService storeDetailService;

    @GetMapping
    public List<StoreDetail> getAllStoreDetails() {
        return storeDetailService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<StoreDetail> getStoreDetailById(@PathVariable Long id) {
        return storeDetailService.findById(id);
    }

    @PostMapping
    public ResponseEntity<StoreDetailDTO> createStoreDetail(@RequestBody StoreDetail storeDetail) {
        StoreDetail savedStoreDetail = storeDetailService.saveStoreDetail(storeDetail);
        StoreDetailDTO dto = new StoreDetailDTO(savedStoreDetail.getStoreDetailId(), savedStoreDetail.getStore().getStoreId());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StoreDetailDTO>> updateStoreDetail(@PathVariable Long id,
                                                                         @RequestBody StoreDetail storeDetail) {
        try {
            StoreDetail updatedStoreDetail = storeDetailService.updateStoreDetail(id, storeDetail);
            StoreDetailDTO dto = new StoreDetailDTO(updatedStoreDetail.getStoreDetailId(),
                    updatedStoreDetail.getStore().getStoreId());
            ApiResponse<StoreDetailDTO> response = new ApiResponse<>("StoreDetail successfully updated", dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<StoreDetailDTO> response = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<StoreDetailDTO>> partialUpdateStoreDetail(@PathVariable Long id,
                                                                                @RequestBody Map<String, Object> updates) {
        try {
            StoreDetail updatedStoreDetail = storeDetailService.partialUpdateStoreDetail(id, updates);
            StoreDetailDTO dto = new StoreDetailDTO(updatedStoreDetail.getStoreDetailId(),
                    updatedStoreDetail.getStore().getStoreId());
            ApiResponse<StoreDetailDTO> response = new ApiResponse<>("StoreDetail successfully updated", dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<StoreDetailDTO> response = new ApiResponse<>("Error updating StoreDetail: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteStoreDetail(@PathVariable Long id) {
        storeDetailService.deleteById(id);
    }
}
