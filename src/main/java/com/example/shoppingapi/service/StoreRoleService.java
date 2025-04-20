package com.example.shoppingapi.service;

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

    public StoreRole findById(StoreRoleId id) {
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

    public StoreRole updateStoreRole(StoreRoleId id, StoreRole storeRole) {
        findById(id);  // parents + existing
        storeRole.setId(id);
        return storeRoleRepository.save(storeRole);
    }

    public StoreRole partialUpdateStoreRole(StoreRoleId id, Map<String, Object> updates) {
        findById(id);
        StoreRole existing = storeRoleRepository.findById(id).get();
        BeanWrapper wrapper = new BeanWrapperImpl(existing);
        updates.forEach(wrapper::setPropertyValue);
        return storeRoleRepository.save(existing);
    }

    public void deleteById(StoreRoleId id) {
        findById(id);
        storeRoleRepository.deleteById(id);
    }
}
