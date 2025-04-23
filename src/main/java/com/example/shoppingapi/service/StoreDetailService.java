package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.patch.StoreDetailPatchDTO;
import com.example.shoppingapi.dto.put.StoreDetailPutDTO;
import com.example.shoppingapi.model.*;
import com.example.shoppingapi.repository.StoreDetailRepository;
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

    public List<StoreDetail> findAll() {
        return storeDetailRepository.findAll();
    }

    public StoreDetail getStoreDetailById(Long id) {
        return storeDetailRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("StoreDetail not found with ID: " + id));
    }

    public StoreDetail saveStoreDetail(StoreDetail detail) {
        return storeDetailRepository.save(detail);
    }

    // Update method for StoreDetailService
    public StoreDetail updateStoreDetail(Long id, StoreDetailPutDTO storeDetailPutDTO) {
        StoreDetail existingStoreDetail = getStoreDetailById(id);
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
    public StoreDetail updateStoreDetail(Long id, StoreDetailPatchDTO storeDetailPatchDTO) {
        StoreDetail existingStoreDetail = getStoreDetailById(id);
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

    // Delete method for StoreDetailService
    public void deleteStoreDetailById(Long id) {
        StoreDetail storeDetail = getStoreDetailById(id);
        storeDetailRepository.delete(storeDetail);
    }
}
