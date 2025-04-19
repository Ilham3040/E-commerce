package com.example.shoppingapi.service;

import com.example.shoppingapi.model.StoreRole;
import com.example.shoppingapi.model.StoreRoleId;
import com.example.shoppingapi.repository.StoreRepository;
import com.example.shoppingapi.repository.StoreRoleRepository;
import com.example.shoppingapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StoreRoleService {

    @Autowired
    private StoreRoleRepository storeRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    public List<StoreRole> findAll() {
        return storeRoleRepository.findAll();
    }

    public Optional<StoreRole> findById(StoreRoleId id) {

        storeRepository.findById(id.getStoreId()).orElseThrow(() -> new EntityNotFoundException("Store not found: " + id.getStoreId()));
        userRepository.findById(id.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found: " + id.getUserId()));
    
        return storeRoleRepository.findById(id);
    }

    public StoreRole saveStoreRole(StoreRole storeRole) {

        storeRepository.findById(storeRole.getStore().getStoreId()).orElseThrow(() -> new EntityNotFoundException("Store not found: " +
        storeRole.getStore().getStoreId() +" cannot create store role "));
        userRepository.findById(storeRole.getUser().getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found: " +
        storeRole.getStore().getStoreId() +" cannot create store role "));

        return storeRoleRepository.save(storeRole);
    }

    public StoreRole updateStoreRole(StoreRoleId id, StoreRole storeRole) {

        storeRepository.findById(id.getStoreId()).orElseThrow(() -> new EntityNotFoundException("Store not found: " + id.getStoreId()));
        userRepository.findById(id.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found: " + id.getUserId()));

        Optional<StoreRole> existingOpt = storeRoleRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("StoreRole not found with given composite key");
        }
        storeRole.setId(id);
        return storeRoleRepository.save(storeRole);
    }

    public StoreRole partialUpdateStoreRole(StoreRoleId id, Map<String, Object> updates) {

        storeRepository.findById(id.getStoreId()).orElseThrow(() -> new EntityNotFoundException("Store not found: " + id.getStoreId()));
        userRepository.findById(id.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found: " + id.getUserId()));

        Optional<StoreRole> existingOpt = storeRoleRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("StoreRole not found with given composite key");
        }
        StoreRole storeRole = existingOpt.get();
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(StoreRole.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, storeRole, value);
            }
        });
        return storeRoleRepository.save(storeRole);
    }

    public void deleteById(StoreRoleId id) {

        storeRepository.findById(id.getStoreId()).orElseThrow(() -> new EntityNotFoundException("Store not found: " + id.getStoreId()));
        userRepository.findById(id.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found: " + id.getUserId()));


        if (!storeRoleRepository.existsById(id)) {
        throw new ResourceNotFoundException("Store Role not found with ID: " + id);
    }

        storeRoleRepository.deleteById(id);
    }
}
