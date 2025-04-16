package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.StoreRepository;
import com.example.shoppingapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.time.LocalDateTime;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository usersRepository;

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public Optional<Store> getStoreById(Long id) {
        return storeRepository.findById(id);
    }

    public Store saveStore(Store store) {
        if (store.getUser() == null || store.getUser().getUserId() == null) {
            throw new IllegalArgumentException("User ID is required to create a store.");
        }

        Optional<User> user = usersRepository.findById(store.getUser().getUserId());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User with ID : " + store.getUser().getUserId() +" not found. Cannot create store.");
        }

        return storeRepository.save(store);
    }

    public Store updateStore(Long id, Store store) {
        if (!id.equals(store.getStoreId())) {
            throw new IllegalArgumentException("Store ID in URL and body must match.");
        }

        Optional<Store> existingStoreOpt = storeRepository.findById(id);
        if (existingStoreOpt.isEmpty()) {
            throw new IllegalArgumentException("Store not found with ID: " + id);
        }
        if (store.getUser() == null || store.getUser().getUserId() == null) {
            throw new IllegalArgumentException("User ID is required to create a store.");
        }

        Optional<User> user = usersRepository.findById(store.getUser().getUserId());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User with ID : " + store.getUser().getUserId() +" not found. Cannot create store.");
        }
        Store updatedStore = store;
        updatedStore.setStoreId(id);

        return storeRepository.save(updatedStore);
    }

    public Store partialUpdateStore(Long id, Map<String, Object> updates) {
        
        Optional<Store> existingStoreOpt = storeRepository.findById(id);
        if (existingStoreOpt.isEmpty()) {
            throw new IllegalArgumentException("Store not found with ID: " + id);
        }

        Store existingStore = existingStoreOpt.get();
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Store.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, existingStore, value);
            }
        });

        return storeRepository.save(existingStore);
    }

    public Store deleteById(Long id) {
        Store existingStore = storeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + id));

        existingStore.setDeletedAt(LocalDateTime.now());
        return storeRepository.save(existingStore);
    }
}
