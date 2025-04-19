package com.example.shoppingapi.service;

import com.example.shoppingapi.model.StoreDetail;
import com.example.shoppingapi.repository.StoreDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StoreDetailService {

    private final StoreDetailRepository storeDetailRepository;

    public List<StoreDetail> findAll() {
        return storeDetailRepository.findAll();
    }

    public StoreDetail findById(Long id) {
        return storeDetailRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("StoreDetail not found with ID: " + id));
    }

    public StoreDetail saveStoreDetail(StoreDetail detail) {
        return storeDetailRepository.save(detail);
    }

    public StoreDetail updateStoreDetail(Long id, StoreDetail detail) {
        findById(id);
        detail.setStoreDetailId(id);
        return storeDetailRepository.save(detail);
    }

    public StoreDetail partialUpdateStoreDetail(Long id, Map<String, Object> updates) {
        StoreDetail existing = findById(id);
        BeanWrapper wrapper = new BeanWrapperImpl(existing);
        updates.forEach(wrapper::setPropertyValue);
        return storeDetailRepository.save(existing);
    }

    public StoreDetail softDeleteStoreDetail(Long id) {
        StoreDetail existing = findById(id);
        existing.setDeletedAt(LocalDateTime.now());
        return storeDetailRepository.save(existing);
    }
}
