package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.StoreRoleCreateDTO;
import com.example.shoppingapi.dto.patch.StoreRolePatchDTO;
import com.example.shoppingapi.dto.put.StoreRolePutDTO;
import com.example.shoppingapi.global.exception.OwnerRoleDeletionException;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.StoreRole;
import com.example.shoppingapi.model.StoreRoleId;
import com.example.shoppingapi.model.User;
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
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreRoleService {

    private final StoreRoleRepository storeRoleRepository;
    private final UserRepository      userRepository;
    private final StoreRepository     storeRepository;

    public List<StoreRole> getAllStoreRoleByStoreId(Long id) {
        storeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + id));
        return storeRoleRepository.findByIdStoreId(id);
    }

    public List<StoreRole> getAllStoreRoleByUserId(Long id) {
        userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return storeRoleRepository.findByIdUserId(id);
    }

    public StoreRole promoteToAdminStoreRole(StoreRoleCreateDTO storeRoleCreateDTO) {
        storeRepository.findById(storeRoleCreateDTO.getStoreId())
            .orElseThrow(() ->
                new ResourceNotFoundException("Store not found with ID: " + storeRoleCreateDTO.getStoreId()));
        userRepository.findById(storeRoleCreateDTO.getUserId())
            .orElseThrow(() ->
                new ResourceNotFoundException("User not found with ID: " + storeRoleCreateDTO.getUserId()));

        StoreRoleId newStoreRoleId = StoreRoleId.builder().storeId(storeRoleCreateDTO.getStoreId()).userId(storeRoleCreateDTO.getUserId()).build();

        StoreRole newStoreRole = StoreRole.builder()
                .id(newStoreRoleId)
                .store(Store.builder().storeId(storeRoleCreateDTO.getStoreId()).build())
                .user(User.builder().userId(storeRoleCreateDTO.getUserId()).build())
                .role("admin")
                .build();
        return storeRoleRepository.save(newStoreRole);
    }

    public void deleteStoreRoleById(StoreRoleId id) {
        StoreRole role = storeRoleRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Store Role of User with ID: " + String.valueOf(id.getUserId()) + " of Store with ID: " + String.valueOf(id.getStoreId()))
        );
        if (Objects.equals(role.getRole(), "owner")){
            throw new OwnerRoleDeletionException("Deleting owner is not allowed");
        }
        storeRoleRepository.delete(role);
    }
}
