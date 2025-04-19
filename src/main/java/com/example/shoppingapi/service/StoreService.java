package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.StoreRepository;
import com.example.shoppingapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public Store getStoreById(Long id) {
        return storeRepository.findById(id)
            .orElseThrow(() -> 
                new ResourceNotFoundException("Store not found with ID: " + id));
    }

    public Store saveStore(Store store) {
        Long userId = Optional.ofNullable(store.getUser())
                              .map(User::getUserId)
                              .orElseThrow(() ->
                                  new IllegalArgumentException("User ID is required to create a store."));

        userRepository.findById(userId)
            .orElseThrow(() ->
                new ResourceNotFoundException("User not found with ID: " + userId));

        return storeRepository.save(store);
    }

    public Store updateStore(Long id, Store store) {
        if (!id.equals(store.getStoreId())) {
            throw new IllegalArgumentException("Store ID in URL and body must match.");
        }

        getStoreById(id);  // throws if missing

        Long userId = Optional.ofNullable(store.getUser())
                              .map(User::getUserId)
                              .orElseThrow(() ->
                                  new IllegalArgumentException("User ID is required to update a store."));

        userRepository.findById(userId)
            .orElseThrow(() ->
                new ResourceNotFoundException("User not found with ID: " + userId));

        store.setStoreId(id);
        return storeRepository.save(store);
    }

    public Store partialUpdateStore(Long id, Map<String, Object> updates) {
        Store existing = getStoreById(id);  // throws if missing

        BeanWrapper wrapper = new BeanWrapperImpl(existing);
        updates.forEach((prop, val) ->
            wrapper.setPropertyValue(prop, val)
        );

        return storeRepository.save(existing);
    }

    public Store softDeleteStore(Long id) {
        Store existing = getStoreById(id);  // throws if missing
        existing.setDeletedAt(LocalDateTime.now());
        return storeRepository.save(existing);
    }
}
