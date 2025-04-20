package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.StoreRole;
import com.example.shoppingapi.model.StoreRoleId;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
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

    @Mock private StoreRoleRepository roleRepo;
    @Mock private UserRepository      userRepo;
    @Mock private StoreRepository     storeRepo;
    @InjectMocks private StoreRoleService service;

    private final ModelHelper<StoreRole> helper =
        ModelHelperFactory.getModelHelper(StoreRole.class);

    @Test
    void findById_notFound_throws() {
        StoreRoleId id = new StoreRoleId(9L, 9L);

        when(storeRepo.findById(id.getStoreId()))
            .thenReturn(Optional.of(Store.builder().storeId(id.getStoreId()).build()));
        when(userRepo.findById(id.getUserId()))
            .thenReturn(Optional.of(User.builder().userId(id.getUserId()).build()));
        // but the role itself is missing
        when(roleRepo.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.findById(id)
        );
        assertEquals("StoreRole not found with ID: " + id, ex.getMessage());

        verify(storeRepo).findById(id.getStoreId());
        verify(userRepo).findById(id.getUserId());
        verify(roleRepo).findById(id);
    }

    @Test
    void deleteById_existing_deletes() {
        StoreRole r  = helper.createModel(1);
        StoreRoleId id = r.getId();

        // parents and role exist
        when(storeRepo.findById(id.getStoreId()))
            .thenReturn(Optional.of(r.getStore()));
        when(userRepo.findById(id.getUserId()))
            .thenReturn(Optional.of(r.getUser()));
        when(roleRepo.findById(id)).thenReturn(Optional.of(r));

        // perform delete
        service.deleteById(id);

        verify(roleRepo).deleteById(id);
    }

    @Test
    void deleteById_notFound_throws() {
        StoreRole r   = helper.createModel(1);
        StoreRoleId id = r.getId();

        // parents exist
        when(storeRepo.findById(id.getStoreId()))
            .thenReturn(Optional.of(r.getStore()));
        when(userRepo.findById(id.getUserId()))
            .thenReturn(Optional.of(r.getUser()));
        // but role is missing
        when(roleRepo.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.deleteById(id)
        );
        assertEquals("StoreRole not found with ID: " + id, ex.getMessage());

        verify(roleRepo).findById(id);
        verify(roleRepo, never()).deleteById(any());
    }
}
