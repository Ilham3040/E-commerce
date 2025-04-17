package com.example.shoppingapi.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.StoreRepository;
import com.example.shoppingapi.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = StoreServiceTests.class)
public class StoreServiceTests {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StoreService storeService;

    private ModelHelper<Store> storeHelper =  ModelHelperFactory.getModelHelper(Store.class);


    @Test
    public void testGetAllStore() {

        Store store1 = storeHelper.createModel(1);
        Store store2 = storeHelper.createModel(2);

        List<Store> mockStores = Arrays.asList(store1,store2);
        when(storeRepository.findAll()).thenReturn(mockStores);

        List<Store> result = storeService.getAllStores();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mockStores, result);

        verify(storeRepository, times(1)).findAll();

    }

    @Test
    public void testGetStore(){
        Store store = storeHelper.createModel(1);
        when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));
        Store result = storeService.getStoreById(store.getStoreId()).orElseThrow(() -> new AssertionError("Store not found"));

        assertEquals(store, result);
        verify(storeRepository, times(1)).findById(store.getStoreId());

    }


    @Test
    public void testGetStorerById_NotFound() {
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Store> result = storeService.getStoreById(1L);

        assertFalse(result.isPresent());
        verify(storeRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateStore() {
        Store store = storeHelper.createModel(1);

        when(storeRepository.save(any(Store.class))).thenReturn(store);
        when(userRepository.findById(store.getUser().getUserId())).thenReturn(Optional.of(store.getUser()));

        Store createdStore = storeService.saveStore(store);

        assertNotNull(createdStore);
        assertEquals(store, createdStore);

        verify(userRepository, times(1)).findById(store.getUser().getUserId());
        verify(storeRepository, times(1)).save(store);
    }


    @Test
    public void testUpdateStore() {
        Store existing = storeHelper.createModel(1);
        Store updatedPayload = existing.toBuilder().storeName("bakul pakan").build();
        when(storeRepository.findById(existing.getStoreId()))
            .thenReturn(Optional.of(existing));
        when(userRepository.findById(existing.getUser().getUserId()))
            .thenReturn(Optional.of(existing.getUser()));
        when(storeRepository.save(any(Store.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        Store result = storeService.updateStore(existing.getStoreId(), updatedPayload);
        assertNotNull(result);
        assertEquals("bakul pakan", result.getStoreName());
        verify(storeRepository, times(1)).findById(existing.getStoreId());
        verify(userRepository, times(1)).findById(existing.getUser().getUserId());
        verify(storeRepository, times(1)).save(updatedPayload);
    }
    
    @Test
    public void testPartialUpdateStore() {
        Store existing = storeHelper.createModel(1);
        when(storeRepository.findById(existing.getStoreId()))
            .thenReturn(Optional.of(existing));
        when(storeRepository.save(any(Store.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        Map<String, Object> updates = Map.of(
            "storeName", "Sniper Kudus 69, One shot one kill"
        );
        Store result = storeService.partialUpdateStore(existing.getStoreId(), updates);
        assertNotNull(result);
        assertEquals("Sniper Kudus 69, One shot one kill", result.getStoreName());
        verify(storeRepository, times(1)).findById(existing.getStoreId());
        verify(storeRepository, times(1)).save(existing);
    }
    

    @Test
    public void testSoftdeleteById() {
        Store store = storeHelper.createModel(1);
        
        when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));
        when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Store deletedStore = storeService.deleteById(store.getStoreId());

        assertNotNull(deletedStore);
        assertNotNull(deletedStore.getDeletedAt());
        assertEquals(store.getStoreId(), deletedStore.getStoreId());

        verify(storeRepository, times(1)).save(deletedStore);
        verify(storeRepository, times(1)).findById(store.getStoreId());
    }

    @Test
    public void testSoftdeleteById_NotFound() {
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            storeService.deleteById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals("Store not found with ID: 1", e.getMessage());
        }

        verify(storeRepository, times(1)).findById(1L);
    }
}
