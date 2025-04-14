package com.example.shoppingapi.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        assertEquals(store1.getStoreId(), result.get(0).getStoreId());
        assertEquals(store1.getStoreName(), result.get(0).getStoreName());
        assertEquals(store1.getUser(), result.get(0).getUser());
        
        assertEquals(store2.getStoreId(), result.get(1).getStoreId());
        assertEquals(store2.getStoreName(), result.get(1).getStoreName());
        assertEquals(store2.getUser(), result.get(1).getUser());

        verify(storeRepository, times(1)).findAll();

    }

    @Test
    public void testGetStore(){
        Store store = storeHelper.createModel(1);

        when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));

        Optional<Store> result = storeService.getStoreById(store.getStoreId());

        assertTrue(result.isPresent());
        assertEquals(store.getStoreId(), result.get().getStoreId());
        assertEquals(store.getStoreName(), result.get().getStoreName());
        assertEquals(store.getUser(), result.get().getUser());

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
        assertEquals(store.getStoreId(), createdStore.getStoreId());
        assertEquals(store.getStoreName(), createdStore.getStoreName());
        assertEquals(store.getUser(), createdStore.getUser());

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
        assertEquals(store.getStoreId(), savedStore.getStoreId());
        assertEquals(store.getStoreName(), savedStore.getStoreName());
        assertEquals(store.getUser(), savedStore.getUser());

        when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));
        
        Store updatedStore = store.toBuilder().storeName("bakul pakan").build();
        
        Store result = storeService.updateStore(store.getStoreId(), updatedStore);
        
        assertNotNull(result);
        assertEquals(store.getStoreId(), result.getStoreId());
        assertEquals(updatedStore.getStoreName(), result.getStoreName());
        assertEquals(store.getUser(), result.getUser());
        
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
        assertEquals(store.getStoreId(), savedStore.getStoreId());
        assertEquals(store.getStoreName(), savedStore.getStoreName());
        assertEquals(store.getUser(), savedStore.getUser());

        when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));

        Map<String, Object> updatedStore = Map.of(

            "storeName","Sniper Kudus 69, One shot one kill"
        );
        
        Store result = storeService.partialUpdateStore(store.getStoreId(), updatedStore);
        
        assertNotNull(result);
        assertEquals(store.getStoreId(), result.getStoreId());
        assertEquals("Sniper Kudus 69, One shot one kill", result.getStoreName());
        assertEquals(store.getUser(), result.getUser());
        
        verify(storeRepository, times(2)).save(any(Store.class));
        verify(storeRepository, times(1)).findById(eq(store.getStoreId()));
        verify(userRepository, times(1)).findById(eq(store.getUser().getUserId()));
    }


}
