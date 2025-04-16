package com.example.shoppingapi.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;

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
    public void testSaveThenUpdateUser() {
        Store store = storeHelper.createModel(1);
        
        when(storeRepository.save(any(Store.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        when(userRepository.findById(store.getUser().getUserId())).thenReturn(Optional.of(store.getUser()));
        
        Store savedStore = storeService.saveStore(store);
        
        assertNotNull(savedStore);
        assertEquals(store, savedStore);

        when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));
        
        Store updatedStore = store.toBuilder().storeName("bakul pakan").build();
        Store result = storeService.updateStore(store.getStoreId(), updatedStore);
        
        assertNotNull(result);
        assertEquals(updatedStore, result);
        
        verify(storeRepository, times(2)).save(any(Store.class));
        verify(storeRepository, times(1)).findById(eq(store.getStoreId()));
        verify(userRepository, times(2)).findById(eq(store.getUser().getUserId()));
    }

    @Test
    public void testSaveThenPartiallyUpdateStore() {
        Store store = storeHelper.createModel(1);
        
        when(storeRepository.save(any(Store.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        when(userRepository.findById(store.getUser().getUserId())).thenReturn(Optional.of(store.getUser()));
        
        Store savedStore = storeService.saveStore(store);
        
        assertNotNull(savedStore);
        assertEquals(store, savedStore);

        when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));

        Map<String, Object> updatedStore = Map.of(

            "storeName","Sniper Kudus 69, One shot one kill"
        );
        
        Store result = storeService.partialUpdateStore(store.getStoreId(), updatedStore);
        
        assertNotNull(result);
        assertEquals("Sniper Kudus 69, One shot one kill", result.getStoreName());
        
        verify(storeRepository, times(2)).save(any(Store.class));
        verify(storeRepository, times(1)).findById(eq(store.getStoreId()));
        verify(userRepository, times(1)).findById(eq(store.getUser().getUserId()));
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
