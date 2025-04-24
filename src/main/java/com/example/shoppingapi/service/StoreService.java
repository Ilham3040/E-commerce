package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.StoreCreateDTO;
import com.example.shoppingapi.dto.patch.StorePatchDTO;
import com.example.shoppingapi.dto.put.StorePutDTO;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.StoreRepository;
import com.example.shoppingapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    public Store saveStore(StoreCreateDTO storeCreateDTO) {
        Long userId = storeCreateDTO.getUserId();
        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with ID: " + userId));

        Store store = Store.builder()
                .storeName(storeCreateDTO.getStoreName())
                .user(User.builder().userId(storeCreateDTO.getUserId()).build())
                .build();

        return storeRepository.save(store);
    }

    public Store updateStore(Long id, StorePutDTO storePutDTO) {
        Store existingStore = getStoreById(id);
        ReflectionUtils.doWithFields(StorePutDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(storePutDTO);
            if (value != null) {
                Field storeField = ReflectionUtils.findField(Store.class, field.getName());
                if (storeField != null) {
                    storeField.setAccessible(true);
                    storeField.set(existingStore, value);
                }
            }
        });
        return storeRepository.save(existingStore);
    }

    public Store partialUpdateStore(Long id, StorePatchDTO storePatchDTO) {
        Store existingStore = getStoreById(id);
        ReflectionUtils.doWithFields(StorePatchDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(storePatchDTO);
            if (value != null) {
                Field storeField = ReflectionUtils.findField(Store.class, field.getName());
                if (storeField != null) {
                    storeField.setAccessible(true);
                    storeField.set(existingStore, value);
                }
            }
        });
        return storeRepository.save(existingStore);
    }

    public void deleteById(Long id) {
        Store store = getStoreById(id);
        storeRepository.delete(store);
    }

}
