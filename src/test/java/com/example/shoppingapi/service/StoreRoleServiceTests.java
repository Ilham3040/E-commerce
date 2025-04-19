package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.StoreRole;
import com.example.shoppingapi.model.StoreRoleId;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.StoreRepository;
import com.example.shoppingapi.repository.StoreRoleRepository;
import com.example.shoppingapi.repository.UserRepository;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = StoreRoleServiceTests.class)
public class StoreRoleServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StoreRoleRepository storeRoleRepository;

    @InjectMocks
    private StoreRoleService storeRoleService;

    private final ModelHelper<StoreRole> storeRoleHelper =
        ModelHelperFactory.getModelHelper(StoreRole.class);

    @Test
    public void testFindAll() {
        StoreRole storeRole1 = storeRoleHelper.createModel(1);
        StoreRole storeRole2 = storeRoleHelper.createModel(2);

        List<StoreRole> mockList = Arrays.asList(storeRole1, storeRole2);

        when(storeRoleRepository.findAll()).thenReturn(mockList);

        List<StoreRole> result = storeRoleService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(storeRole1, result.get(0));
        assertEquals(storeRole2, result.get(1));

        verify(storeRoleRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        StoreRole storeRole = storeRoleHelper.createModel(1);
        StoreRoleId id = storeRole.getId();
        when(storeRoleRepository.findById(storeRole.getId()))
            .thenReturn(Optional.of(storeRole));
        when(userRepository.findById(id.getUserId())).thenReturn(Optional.of(storeRole.getUser()));
        when(storeRepository.findById(id.getStoreId())).thenReturn(Optional.of(storeRole.getStore()));

        StoreRole result = storeRoleService.findById(storeRole.getId()).orElseThrow(() -> new AssertionError("Cart Item doesn't exist"));
        assertNotNull(result);
        assertEquals(storeRole, result);

        verify(storeRoleRepository, times(1)).findById(storeRole.getId());
        verify(userRepository, times(1)).findById(id.getUserId());
        verify(storeRepository, times(1)).findById(id.getStoreId());
    }

    @Test
    public void testFindById_NotFound() {
        StoreRoleId missingId = new StoreRoleId(9L, 9L);

        when(storeRepository.findById(missingId.getStoreId()))
            .thenReturn(Optional.of(Store.builder().storeId(9L).build()));
        when(userRepository.findById(missingId.getUserId()))
            .thenReturn(Optional.of(User.builder().userId(9L).build()));

        when(storeRoleRepository.findById(missingId)).thenReturn(Optional.empty());

        Optional<StoreRole> result = storeRoleService.findById(missingId);

        assertFalse(result.isPresent());

        verify(storeRoleRepository, times(1)).findById(missingId);
        verify(storeRepository, times(1)).findById(missingId.getStoreId());
        verify(userRepository, times(1)).findById(missingId.getUserId());
    }

    @Test
    public void testSaveStoreRole() {
        StoreRole storeRole = storeRoleHelper.createModel(1);

        when(storeRepository.findById(storeRole.getStore().getStoreId()))
            .thenReturn(Optional.of(storeRole.getStore()));
        when(userRepository.findById(storeRole.getUser().getUserId()))
            .thenReturn(Optional.of(storeRole.getUser()));
        when(storeRoleRepository.save(any(StoreRole.class))).thenReturn(storeRole);

        StoreRole created = storeRoleService.saveStoreRole(storeRole);
        
        assertNotNull(created);
        assertEquals(storeRole, created);

        verify(storeRoleRepository, times(1)).save(storeRole);
        verify(storeRepository, times(1)).findById(storeRole.getStore().getStoreId());
        verify(userRepository, times(1)).findById(storeRole.getUser().getUserId());
    }

    @Test
    public void testUpdateStoreRole() {
        StoreRole existing = storeRoleHelper.createModel(1);
        StoreRoleId id = existing.getId();

        when(storeRoleRepository.findById(existing.getId())).thenReturn(Optional.of(existing));
        when(storeRoleRepository.save(any(StoreRole.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.findById(id.getUserId())).thenReturn(Optional.of(existing.getUser()));
        when(storeRepository.findById(id.getStoreId())).thenReturn(Optional.of(existing.getStore()));
        
        StoreRole updated  = existing.toBuilder().role("admin").build();
        StoreRole result = storeRoleService.updateStoreRole(existing.getId(), updated);

        assertNotNull(result);
        assertEquals("admin", result.getRole());

        verify(storeRoleRepository, times(1)).findById(existing.getId());
        verify(storeRoleRepository, times(1)).save(updated);
        verify(userRepository, times(1)).findById(id.getUserId());
        verify(storeRepository, times(1)).findById(id.getStoreId());
    }

    @Test
    public void testPartialUpdateStoreRole() {
        StoreRole storeRole = storeRoleHelper.createModel(2);
        StoreRoleId id = storeRole.getId();

        when(storeRoleRepository.findById(id)).thenReturn(Optional.of(storeRole));
        when(storeRoleRepository.save(any(StoreRole.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.findById(id.getUserId())).thenReturn(Optional.of(storeRole.getUser()));
        when(storeRepository.findById(id.getStoreId())).thenReturn(Optional.of(storeRole.getStore()));

        Map<String, Object> updates = Map.of("role", "admin");
        StoreRole result = storeRoleService.partialUpdateStoreRole(id, updates);

        assertNotNull(result);
        assertEquals("admin", result.getRole());

        verify(userRepository, times(1)).findById(id.getUserId());
        verify(storeRepository, times(1)).findById(id.getStoreId());
        verify(storeRoleRepository, times(1)).findById(id);
        verify(storeRoleRepository, times(1)).save(storeRole);
    }

        @Test
        public void testDeleteStoreRole() {
        StoreRole storeRole  = storeRoleHelper.createModel(1);
        StoreRoleId id = storeRole.getId();

        when(storeRoleRepository.existsById(id))
            .thenReturn(true);
        doNothing().when(storeRoleRepository).deleteById(id);
        when(userRepository.findById(id.getUserId())).thenReturn(Optional.of(storeRole.getUser()));
        when(storeRepository.findById(id.getStoreId())).thenReturn(Optional.of(storeRole.getStore()));

        storeRoleService.deleteById(id);

        verify(userRepository, times(1)).findById(id.getUserId());
        verify(storeRepository, times(1)).findById(id.getStoreId());
        verify(storeRoleRepository, times(1)).existsById(id);
        verify(storeRoleRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteStoreRole_NotFound() {
        StoreRoleId id = new StoreRoleId(9L, 9L);
    
        
        when(storeRepository.findById(id.getStoreId()))
            .thenReturn(Optional.of(Store.builder().storeId(9L).build()));
        when(userRepository.findById(id.getUserId()))
            .thenReturn(Optional.of(User.builder().userId(9L).build()));
    
        
        when(storeRoleRepository.existsById(id)).thenReturn(false);
    
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> storeRoleService.deleteById(id)
        );
    
        assertEquals("Store Role not found with ID: " + id, ex.getMessage());
    
        
        verify(storeRepository, times(1)).findById(id.getStoreId());
        verify(userRepository, times(1)).findById(id.getUserId());
        verify(storeRoleRepository, times(1)).existsById(id);
        verify(storeRoleRepository, never()).deleteById(any());
    }
    
}
