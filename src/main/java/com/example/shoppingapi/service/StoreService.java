package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.StoreRepository;
import com.example.shoppingapi.repository.UserRepository;
import org.springframework.util.ReflectionUtils;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.lang.reflect.Field;

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
            throw new IllegalArgumentException("User not found. Cannot create store.");
        }

        return storeRepository.save(store);
    }

    public Store updateStore(Long id, Store store) {
        Optional<Store> existingStoreOpt = storeRepository.findById(id);
        if (existingStoreOpt.isEmpty()) {
            throw new IllegalArgumentException("Store not found with ID: " + id);
        }
        Store existingStore = existingStoreOpt.get();
        existingStore.setUser(store.getUser());
        // Set other fields as needed
        return storeRepository.save(existingStore);
    }

    // Partial Update (Direct Mapping)
    public Store partialUpdateStore(Long id, Map<String, Object> updates) {
        Optional<Store> existingStoreOpt = storeRepository.findById(id);
        if (existingStoreOpt.isEmpty()) {
            throw new IllegalArgumentException("Store not found with ID: " + id);
        }
        Store existingStore = existingStoreOpt.get();

        // Iterate over the provided updates and apply them to the existing store
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Store.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, existingStore, value);
            }
        });

        return storeRepository.save(existingStore);
    }
    

    public void deleteStore(Long id) {
        storeRepository.deleteById(id);
    }
}
