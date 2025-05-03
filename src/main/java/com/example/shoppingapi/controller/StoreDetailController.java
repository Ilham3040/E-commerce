package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.create.StoreDetailCreateDTO;
import com.example.shoppingapi.dto.put.StoreDetailPutDTO;
import com.example.shoppingapi.dto.patch.StoreDetailPatchDTO;
import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.dto.response.StoreDetailDTO;
import com.example.shoppingapi.model.StoreDetail;
import com.example.shoppingapi.service.StoreDetailService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/storesdetail")
@RequiredArgsConstructor
public class StoreDetailController {
    private final StoreDetailService storeDetailService;

    @GetMapping
    public ApiResponse<List<StoreDetailDTO>> getAllStoreDetails() {
        List<StoreDetailDTO> storeDetailDTOs = storeDetailService.findAll()
                .stream()
                .map(detail -> new StoreDetailDTO(detail.getStoreDetailId(), detail.getStore().getStoreId()))
                .collect(Collectors.toList());
        return new ApiResponse<>("Successfully fetched all store details", storeDetailDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ApiResponse<StoreDetailDTO> getStoreDetailById(@PathVariable Long id) {
        StoreDetail storeDetail = storeDetailService.getStoreDetailByStoreId(id);
        return new ApiResponse<>("Successfully fetched store detail", new StoreDetailDTO(storeDetail.getStoreDetailId(), storeDetail.getStore().getStoreId()), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<StoreDetailDTO> createStoreDetail(@Validated @RequestBody StoreDetailCreateDTO storeDetailCreateDTO) {
        StoreDetail createdStoreDetail = storeDetailService.saveStoreDetail(storeDetailCreateDTO);
        return new ApiResponse<>("Successfully created store detail", new StoreDetailDTO(createdStoreDetail.getStoreDetailId(), createdStoreDetail.getStore().getStoreId()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ApiResponse<StoreDetailDTO> updateStoreDetail(@PathVariable Long id, @Validated @RequestBody StoreDetailPutDTO storeDetailPutDTO) {
        StoreDetail updatedStoreDetail = storeDetailService.updateStoreDetail(id, storeDetailPutDTO);
        return new ApiResponse<>("Successfully updated store detail", new StoreDetailDTO(updatedStoreDetail.getStoreDetailId(), updatedStoreDetail.getStore().getStoreId()), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ApiResponse<StoreDetailDTO> updatePartiallyStoreDetail(@PathVariable Long id, @RequestBody StoreDetailPatchDTO storeDetailPatchDTO) {
        StoreDetail updatedStoreDetail = storeDetailService.partiallyUpdateStoreDetail(id, storeDetailPatchDTO);
        return new ApiResponse<>("Successfully updated store detail", new StoreDetailDTO(updatedStoreDetail.getStoreDetailId(), updatedStoreDetail.getStore().getStoreId()), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteStoreDetail(@PathVariable Long id) {
        storeDetailService.deleteById(id);
        return new ApiResponse<>("Successfully deleted store detail", null, HttpStatus.NO_CONTENT);
    }
}
