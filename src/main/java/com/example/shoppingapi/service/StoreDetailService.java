package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.StoreDetailCreateDTO;
import com.example.shoppingapi.dto.patch.StoreDetailPatchDTO;
import com.example.shoppingapi.dto.put.StoreDetailPutDTO;
import com.example.shoppingapi.model.*;
import com.example.shoppingapi.repository.StoreDetailRepository;
import com.example.shoppingapi.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StoreDetailService {

    private final StoreDetailRepository storeDetailRepository;
    private final StoreRepository storeRepository;

    public List<StoreDetail> findAll() {
        return storeDetailRepository.findAll();
    }

    public StoreDetail getStoreDetailByStoreId(Long id) {
        return storeDetailRepository.findStoreDetailbyProductId(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("StoreDetail not found with ID: " + id));
    }

    public StoreDetail saveStoreDetail(StoreDetailCreateDTO storeDetailCreateDTO) {
        storeRepository.findById(storeDetailCreateDTO.getStoreId()).orElseThrow(()-> new ResourceNotFoundException("Store not found cannot create Store Detail"));

        StoreDetail storeDetail = StoreDetail.builder()
                .address(storeDetailCreateDTO.getAddress())
                .store(Store.builder().storeId(storeDetailCreateDTO.getStoreId()).build())
                .description(storeDetailCreateDTO.getDescription())
                .build();

        return storeDetailRepository.save(storeDetail);
    }

    public StoreDetail updateStoreDetail(Long id, StoreDetailPutDTO storeDetailPutDTO) {
        StoreDetail existingStoreDetail = getStoreDetailByStoreId(id);
        ReflectionUtils.doWithFields(StoreDetailPutDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(storeDetailPutDTO);
            if (value != null) {
                Field storeDetailField = ReflectionUtils.findField(StoreDetail.class, field.getName());
                if (storeDetailField != null) {
                    storeDetailField.setAccessible(true);
                    storeDetailField.set(existingStoreDetail, value);
                }
            }
        });
        return storeDetailRepository.save(existingStoreDetail);
    }

    // Partial update method for StoreDetailService
    public StoreDetail partiallyUpdateStoreDetail(Long id, StoreDetailPatchDTO storeDetailPatchDTO) {
        StoreDetail existingStoreDetail = getStoreDetailByStoreId(id);
        ReflectionUtils.doWithFields(StoreDetailPatchDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(storeDetailPatchDTO);
            if (value != null) {
                Field storeDetailField = ReflectionUtils.findField(StoreDetail.class, field.getName());
                if (storeDetailField != null) {
                    storeDetailField.setAccessible(true);
                    storeDetailField.set(existingStoreDetail, value);
                }
            }
        });
        return storeDetailRepository.save(existingStoreDetail);
    }

    public void deleteById(Long id) {
        StoreDetail storeDetail = getStoreDetailByStoreId(id);
        storeDetailRepository.delete(storeDetail);
    }
}
