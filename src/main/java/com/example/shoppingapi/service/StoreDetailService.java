package com.example.shoppingapi.service;

import com.example.shoppingapi.model.StoreDetail;
import com.example.shoppingapi.repository.StoreDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StoreDetailService {

    @Autowired
    private StoreDetailRepository storeDetailRepository;

    public List<StoreDetail> findAll() {
        return storeDetailRepository.findAll();
    }

    public Optional<StoreDetail> findById(Long id) {
        return storeDetailRepository.findById(id);
    }

    public StoreDetail saveStoreDetail(StoreDetail storeDetail) {
        return storeDetailRepository.save(storeDetail);
    }

    public StoreDetail updateStoreDetail(Long id, StoreDetail storeDetail) {
        Optional<StoreDetail> existingOpt = storeDetailRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("StoreDetail not found with ID: " + id);
        }
        storeDetail.setStoreDetailId(id);
        return storeDetailRepository.save(storeDetail);
    }

    public StoreDetail partialUpdateStoreDetail(Long id, Map<String, Object> updates) {
        Optional<StoreDetail> existingOpt = storeDetailRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("StoreDetail not found with ID: " + id);
        }
        StoreDetail storeDetail = existingOpt.get();
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(StoreDetail.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, storeDetail, value);
            }
        });
        return storeDetailRepository.save(storeDetail);
    }

    public StoreDetail deleteById(Long id) {
        StoreDetail storeDetail = storeDetailRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("StoreDetail not found with ID: " + id));

        storeDetail.setDeletedAt(LocalDateTime.now());
        storeDetailRepository.save(storeDetail);
        return storeDetail;
    }

}
