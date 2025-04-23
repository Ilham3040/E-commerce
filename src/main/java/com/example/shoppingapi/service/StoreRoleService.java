package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.patch.StoreRolePatchDTO;
import com.example.shoppingapi.dto.put.StoreRolePutDTO;
import com.example.shoppingapi.model.StoreRole;
import com.example.shoppingapi.model.StoreRoleId;
import com.example.shoppingapi.repository.StoreRepository;
import com.example.shoppingapi.repository.StoreRoleRepository;
import com.example.shoppingapi.repository.UserRepository;
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
public class StoreRoleService {

    private final StoreRoleRepository storeRoleRepository;
    private final UserRepository      userRepository;
    private final StoreRepository     storeRepository;

    public List<StoreRole> findAll() {
        return storeRoleRepository.findAll();
    }

    public StoreRole getStoreRoleById(StoreRoleId id) {
        storeRepository.findById(id.getStoreId())
            .orElseThrow(() ->
                new ResourceNotFoundException("Store not found with ID: " + id.getStoreId()));
        userRepository.findById(id.getUserId())
            .orElseThrow(() ->
                new ResourceNotFoundException("User not found with ID: " + id.getUserId()));
        return storeRoleRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("StoreRole not found with ID: " + id));
    }

    public StoreRole saveStoreRole(StoreRole storeRole) {
        storeRepository.findById(storeRole.getStore().getStoreId())
            .orElseThrow(() ->
                new ResourceNotFoundException("Store not found with ID: " + storeRole.getStore().getStoreId()));
        userRepository.findById(storeRole.getUser().getUserId())
            .orElseThrow(() ->
                new ResourceNotFoundException("User not found with ID: " + storeRole.getUser().getUserId()));
        return storeRoleRepository.save(storeRole);
    }

    // Update method for StoreRoleService
    public StoreRole updateStoreRole(StoreRoleId id, StoreRolePutDTO storeRolePutDTO) {
        StoreRole existingStoreRole = getStoreRoleById(id);
        ReflectionUtils.doWithFields(StoreRolePutDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(storeRolePutDTO);
            if (value != null) {
                Field storeRoleField = ReflectionUtils.findField(StoreRole.class, field.getName());
                if (storeRoleField != null) {
                    storeRoleField.setAccessible(true);
                    storeRoleField.set(existingStoreRole, value);
                }
            }
        });
        return storeRoleRepository.save(existingStoreRole);
    }

    public StoreRole partiallyUpdateStoreRole(StoreRoleId id, StoreRolePatchDTO storeRolePatchDTO) {
        StoreRole existingStoreRole = getStoreRoleById(id);
        ReflectionUtils.doWithFields(StoreRolePatchDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(storeRolePatchDTO);
            if (value != null) {
                Field storeRoleField = ReflectionUtils.findField(StoreRole.class, field.getName());
                if (storeRoleField != null) {
                    storeRoleField.setAccessible(true);
                    storeRoleField.set(existingStoreRole, value);
                }
            }
        });
        return storeRoleRepository.save(existingStoreRole);
    }

    // Delete method for StoreRoleService
    public void deleteStoreRoleById(StoreRoleId id) {
        StoreRole storeRole = getStoreRoleById(id);
        storeRoleRepository.delete(storeRole);
    }
}
