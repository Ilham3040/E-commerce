package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.StoreRepository;
import com.example.shoppingapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StoreService storeService;

    private final ModelHelper<Store> storeHelper =
        ModelHelperFactory.getModelHelper(Store.class);

    @Test
    void getAllStores_returnsAllStores() {
        List<Store> expected = List.of(
            storeHelper.createModel(1),
            storeHelper.createModel(2)
        );
        when(storeRepository.findAll()).thenReturn(expected);

        List<Store> actual = storeService.getAllStores();
        assertEquals(expected, actual);
        verify(storeRepository).findAll();
    }

    @Test
    void getStoreById_found_returnsStore() {
        Store expected = storeHelper.createModel(1);
        when(storeRepository.findById(1L)).thenReturn(Optional.of(expected));

        Store actual = storeService.getStoreById(1L);
        assertEquals(expected, actual);
        verify(storeRepository).findById(1L);
    }

    @Test
    void getStoreById_notFound_throwsException() {
        when(storeRepository.findById(2L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> storeService.getStoreById(2L)
        );
        assertEquals("Store not found with ID: 2", ex.getMessage());
        verify(storeRepository).findById(2L);
    }

    @Test
    void saveStore_missingUser_throwsException() {
        Store store = storeHelper.createModel(1);
        store.setUser(null);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> storeService.saveStore(store)
        );
        assertEquals("User ID is required to create a store.", ex.getMessage());
        verify(storeRepository, never()).save(any());
    }

    @Test
    void saveStore_userNotFound_throwsException() {
        Store store = storeHelper.createModel(1);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> storeService.saveStore(store)
        );
        assertEquals("User not found with ID: 1", ex.getMessage());
        verify(userRepository).findById(1L);
        verify(storeRepository, never()).save(any());
    }

    @Test
    void saveStore_success_savesAndReturnsStore() {
        Store store = storeHelper.createModel(1);
        when(userRepository.findById(1L))
            .thenReturn(Optional.of(store.getUser()));
        when(storeRepository.save(any())).thenReturn(store);

        Store saved = storeService.saveStore(store);
        assertEquals(store, saved);
        verify(userRepository).findById(1L);
        verify(storeRepository).save(store);
    }

    @Test
    void updateStore_idMismatch_throwsException() {
        Store store = storeHelper.createModel(1);
        store.setStoreId(2L);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> storeService.updateStore(1L, store)
        );
        assertEquals("Store ID in URL and body must match.", ex.getMessage());
        verify(storeRepository, never()).save(any());
    }

    @Test
    void updateStore_notFound_throwsException() {
        Store store = storeHelper.createModel(1);
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> storeService.updateStore(1L, store)
        );
        assertEquals("Store not found with ID: 1", ex.getMessage());
        verify(storeRepository).findById(1L);
    }

    @Test
    void updateStore_missingUser_throwsException() {
        Store store = storeHelper.createModel(1);
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        store.setUser(null);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> storeService.updateStore(1L, store)
        );
        assertEquals("User ID is required to update a store.", ex.getMessage());
        verify(storeRepository).findById(1L);
        verify(storeRepository, never()).save(any());
    }

    @Test
    void updateStore_userNotFound_throwsException() {
        Store store = storeHelper.createModel(1);
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> storeService.updateStore(1L, store)
        );
        assertEquals("User not found with ID: 1", ex.getMessage());
        verify(userRepository).findById(1L);
        verify(storeRepository, never()).save(any());
    }

    @Test
    void updateStore_success_savesUpdatedStore() {
        Store original = storeHelper.createModel(1);
        Store updated  = original.toBuilder()
                                 .storeName("New Name")
                                 .build();

        when(storeRepository.findById(1L))
            .thenReturn(Optional.of(original));
        when(userRepository.findById(1L))
            .thenReturn(Optional.of(original.getUser()));
        when(storeRepository.save(any()))
            .thenReturn(updated);

        Store result = storeService.updateStore(1L, updated);
        assertEquals("New Name", result.getStoreName());
        verify(storeRepository).findById(1L);
        verify(userRepository).findById(1L);
        verify(storeRepository).save(updated);
    }

    @Test
    void partialUpdateStore_found_appliesUpdates() {
        Store existing = storeHelper.createModel(1);
        when(storeRepository.findById(1L))
            .thenReturn(Optional.of(existing));
        when(storeRepository.save(any()))
            .thenAnswer(inv -> inv.getArgument(0));

        Map<String, Object> updates = Map.of("storeName", "Updated Store");
        Store result = storeService.partialUpdateStore(1L, updates);
        assertEquals("Updated Store", result.getStoreName());
        verify(storeRepository).findById(1L);
        verify(storeRepository).save(existing);
    }

    @Test
    void partialUpdateStore_notFound_throwsException() {
        when(storeRepository.findById(2L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> storeService.partialUpdateStore(2L, Map.of("storeName", "X"))
        );
        assertEquals("Store not found with ID: 2", ex.getMessage());
        verify(storeRepository).findById(2L);
    }

    @Test
    void softDeleteStore_existing_setsDeletedAt() {
        Store existing = storeHelper.createModel(1);
        when(storeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(storeRepository.save(any()))
            .thenAnswer(inv -> inv.getArgument(0));

        Store result = storeService.softDeleteStore(1L);
        assertNotNull(result.getDeletedAt());
        assertTrue(result.getDeletedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        verify(storeRepository).findById(1L);
        verify(storeRepository).save(existing);
    }

    @Test
    void softDeleteStore_notFound_throwsException() {
        when(storeRepository.findById(3L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> storeService.softDeleteStore(3L)
        );
        assertEquals("Store not found with ID: 3", ex.getMessage());
        verify(storeRepository).findById(3L);
    }
}
