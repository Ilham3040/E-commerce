package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.request.StoreRequestDTO;
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

    public Store saveStore(StoreRequestDTO storeRequestDTO) {
        Long userId = storeRequestDTO.getUserId();
        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with ID: " + userId));

        Store store = Store.builder()
                .storeName(storeRequestDTO.getStoreName())
                .user(User.builder().userId(storeRequestDTO.getUserId()).build())
                .build();

        return storeRepository.save(store);
    }

    public Store updateStore(Long id, StoreRequestDTO storeRequestDTO) {
        getStoreById(id);
        Long userId = storeRequestDTO.getUserId();
        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with ID: " + userId));

        Store storeToUpdate = new Store();
        storeToUpdate.setStoreName(storeRequestDTO.getStoreName());

        return storeRepository.save(storeToUpdate);
    }

    public Store partialUpdateStore(Long id, Map<String, Object> updates) {
        Store existing = getStoreById(id);

        BeanWrapper wrapper = new BeanWrapperImpl(existing);
        updates.forEach(wrapper::setPropertyValue);

        return storeRepository.save(existing);
    }

    public Store deleteById(Long id) {
        Store existing = getStoreById(id);
        existing.setDeletedAt(LocalDateTime.now());
        return storeRepository.save(existing);
    }
}
