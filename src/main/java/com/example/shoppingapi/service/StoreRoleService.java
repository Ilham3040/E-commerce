package com.example.shoppingapi.service;

import com.example.shoppingapi.model.StoreRole;
import com.example.shoppingapi.model.StoreRoleId;
import com.example.shoppingapi.repository.StoreRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<StoreRole> findAll() {
        return storeRoleRepository.findAll();
    }

    public Optional<StoreRole> findById(Long userId, Long storeId) {
        StoreRoleId id = new StoreRoleId();
        id.setUserId(userId);
        id.setStoreId(storeId);
        return storeRoleRepository.findById(id);
    }

    public StoreRole saveStoreRole(StoreRole storeRole) {
        return storeRoleRepository.save(storeRole);
    }

    public StoreRole updateStoreRole(Long userId, Long storeId, StoreRole storeRole) {
        StoreRoleId id = new StoreRoleId();
        id.setUserId(userId);
        id.setStoreId(storeId);
        Optional<StoreRole> existingOpt = storeRoleRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("StoreRole not found with given composite key");
        }
        storeRole.setId(id);
        return storeRoleRepository.save(storeRole);
    }

    public StoreRole partialUpdateStoreRole(Long userId, Long storeId, Map<String, Object> updates) {
        StoreRoleId id = new StoreRoleId();
        id.setUserId(userId);
        id.setStoreId(storeId);
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

    public void deleteById(Long userId, Long storeId) {
        StoreRoleId id = new StoreRoleId();
        id.setUserId(userId);
        id.setStoreId(storeId);
        storeRoleRepository.deleteById(id);
    }
}
