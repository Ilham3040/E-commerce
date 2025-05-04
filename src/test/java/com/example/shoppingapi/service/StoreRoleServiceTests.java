package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.StoreRoleCreateDTO;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.StoreRole;
import com.example.shoppingapi.model.StoreRoleId;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.global.exception.OwnerRoleDeletionException;
import com.example.shoppingapi.repository.StoreRepository;
import com.example.shoppingapi.repository.StoreRoleRepository;
import com.example.shoppingapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreRoleServiceTest {

    @Mock private StoreRoleRepository storeRoleRepository;
    @Mock private UserRepository userRepository;
    @Mock private StoreRepository storeRepository;
    @InjectMocks private StoreRoleService storeRoleService;

    @Test
    void getAllStoreRoleByStoreId_storeNotFound_throwsResourceNotFound() {
        Long storeId = 1L;

        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> storeRoleService.getAllStoreRoleByStoreId(storeId)
        );

        assertEquals("Store not found with ID: " + storeId, exception.getMessage());

        verify(storeRepository).findById(storeId);
        verifyNoInteractions(storeRoleRepository); // No need to call roleRepo if store is not found
    }

    @Test
    void deleteStoreRoleById_roleIsAdmin_throwsOwnerRoleDeletionException() {
        StoreRoleId storeRoleId = new StoreRoleId(1L, 1L);
        StoreRole adminRole = new StoreRole();
        adminRole.setRole("owner");
        adminRole.setId(storeRoleId);

        when(storeRoleRepository.findById(storeRoleId)).thenReturn(Optional.of(adminRole));

        OwnerRoleDeletionException exception = assertThrows(
                OwnerRoleDeletionException.class,
                () -> storeRoleService.deleteStoreRoleById(storeRoleId)
        );

        assertEquals("Deleting owner is not allowed", exception.getMessage());

        verify(storeRoleRepository).findById(storeRoleId);
        verifyNoInteractions(storeRepository, userRepository); // No need to interact with storeRepo or userRepo
    }

    @Test
    void deleteStoreRoleById_roleExists_deletesRole() {
        StoreRoleId storeRoleId = new StoreRoleId(1L, 1L);
        StoreRole role = new StoreRole();
        role.setRole("user");
        role.setId(storeRoleId);

        when(storeRoleRepository.findById(storeRoleId)).thenReturn(Optional.of(role));

        storeRoleService.deleteStoreRoleById(storeRoleId);

        verify(storeRoleRepository).delete(role); // Make sure the role gets deleted
    }

    @Test
    void deleteStoreRoleById_roleNotFound_throwsResourceNotFoundException() {
        StoreRoleId storeRoleId = new StoreRoleId(1L, 1L);

        when(storeRoleRepository.findById(storeRoleId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> storeRoleService.deleteStoreRoleById(storeRoleId)
        );

        assertEquals("Store Role of User with ID: " + storeRoleId.getUserId() + " of Store with ID: " + storeRoleId.getStoreId(), exception.getMessage());

        verify(storeRoleRepository).findById(storeRoleId);
        verifyNoInteractions(storeRepository, userRepository); // No need to call storeRepo or userRepo if role is missing
    }

    @Test
    void promoteToAdminStoreRole_validData_createsStoreRole() {
        StoreRoleCreateDTO storeRoleCreateDTO = new StoreRoleCreateDTO();
        storeRoleCreateDTO.setStoreId(1L);
        storeRoleCreateDTO.setUserId(1L);
        Store store = new Store();
        store.setStoreId(1L);
        User user = new User();
        user.setUserId(1L);

        when(storeRepository.findById(storeRoleCreateDTO.getStoreId())).thenReturn(Optional.of(store));
        when(userRepository.findById(storeRoleCreateDTO.getUserId())).thenReturn(Optional.of(user));
        when(storeRoleRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        StoreRole createdStoreRole = storeRoleService.promoteToAdminStoreRole(storeRoleCreateDTO);

        assertNotNull(createdStoreRole);
        assertEquals("admin", createdStoreRole.getRole());

        verify(storeRepository).findById(storeRoleCreateDTO.getStoreId());
        verify(userRepository).findById(storeRoleCreateDTO.getUserId());
        verify(storeRoleRepository).save(createdStoreRole);
    }

    @Test
    void promoteToAdminStoreRole_storeNotFound_throwsResourceNotFound() {
        StoreRoleCreateDTO storeRoleCreateDTO = new StoreRoleCreateDTO();
        storeRoleCreateDTO.setStoreId(1L);
        storeRoleCreateDTO.setUserId(1L);
        when(storeRepository.findById(storeRoleCreateDTO.getStoreId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> storeRoleService.promoteToAdminStoreRole(storeRoleCreateDTO)
        );

        assertEquals("Store not found with ID: " + storeRoleCreateDTO.getStoreId(), exception.getMessage());
    }

    @Test
    void promoteToAdminStoreRole_userNotFound_throwsResourceNotFound() {
        StoreRoleCreateDTO storeRoleCreateDTO = new StoreRoleCreateDTO();
        storeRoleCreateDTO.setStoreId(1L);
        storeRoleCreateDTO.setUserId(1L);
        Store store = new Store();
        store.setStoreId(1L);

        when(storeRepository.findById(storeRoleCreateDTO.getStoreId())).thenReturn(Optional.of(store));
        when(userRepository.findById(storeRoleCreateDTO.getUserId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> storeRoleService.promoteToAdminStoreRole(storeRoleCreateDTO)
        );

        assertEquals("User not found with ID: " + storeRoleCreateDTO.getUserId(), exception.getMessage());
    }
}
