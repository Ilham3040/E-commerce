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
import com.example.shoppingapi.model.User;
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

    private ModelHelper<User> userHelper =  ModelHelperFactory.getModelHelper(User.class);
    private ModelHelper<Store> storeHelper =  ModelHelperFactory.getModelHelper(Store.class);


    @Test
    public void testGetAllStore() {


        User user1 = userHelper.createModel(1);
        User user2 = userHelper.createModel(1);
        Store store1 = storeHelper.createModel(1);
        Store store2 = storeHelper.createModel(2);
        store1.setUser(user1);
        store2.setUser(user2);


        List<Store> mockStores = Arrays.asList(store1,store2);
        when(storeRepository.findAll()).thenReturn(mockStores);

        List<Store> result = storeService.getAllStores();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(store1.getStoreId(), result.get(0).getStoreId());
        assertEquals(store1.getStoreName(), result.get(0).getStoreName());
        assertEquals(store1.getStoreDescription(), result.get(0).getStoreDescription());
        assertEquals(store1.getUser(), result.get(0).getUser());
        
        assertEquals(store2.getStoreId(), result.get(1).getStoreId());
        assertEquals(store2.getStoreName(), result.get(1).getStoreName());
        assertEquals(store2.getStoreDescription(), result.get(1).getStoreDescription());
        assertEquals(store2.getUser(), result.get(1).getUser());

        verify(storeRepository, times(1)).findAll();

    }

    @Test
    public void testGetStore(){
        User user = userHelper.createModel(1);
        Store store = storeHelper.createModel(1);
        store.setUser(user);

        when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));

        Optional<Store> result = storeService.getStoreById(store.getStoreId());

        assertTrue(result.isPresent());
        assertEquals(store.getStoreId(), result.get().getStoreId());
        assertEquals(store.getStoreName(), result.get().getStoreName());
        assertEquals(store.getStoreDescription(), result.get().getStoreDescription());
        assertEquals(user, result.get().getUser());

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
        User user = userHelper.createModel(1);
        Store store = storeHelper.createModel(1);
        store.setUser(user);

        when(storeRepository.save(any(Store.class))).thenReturn(store);
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        Store createdStore = storeService.saveStore(store);

        assertNotNull(createdStore);
        assertEquals(store.getStoreId(), createdStore.getStoreId());
        assertEquals(store.getStoreName(), createdStore.getStoreName());
        assertEquals(store.getStoreDescription(), createdStore.getStoreDescription());
        assertEquals(user, createdStore.getUser());


        verify(userRepository, times(1)).findById(user.getUserId());
        verify(storeRepository, times(1)).save(store);
    }


    @Test
    public void testSaveThenUpdateUser() {
        User user = userHelper.createModel(1);
        Store store = storeHelper.createModel(1);
        store.setUser(user);
        
        when(storeRepository.save(any(Store.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        
        Store savedStore = storeService.saveStore(store);
        
        assertNotNull(savedStore);
        assertEquals(store.getStoreId(), savedStore.getStoreId());
        assertEquals(store.getStoreName(), savedStore.getStoreName());
        assertEquals(store.getStoreDescription(), savedStore.getStoreDescription());
        assertEquals(user, savedStore.getUser());

        when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));
        
        Store updatedStore = new Store();
        updatedStore.setStoreId(store.getStoreId());
        updatedStore.setStoreName(store.getStoreName());
        updatedStore.setStoreDescription("Sniper Kudus 69, One shot one kill");
        updatedStore.setUser(user);
        
        Store result = storeService.updateStore(store.getStoreId(), updatedStore);
        
        assertNotNull(result);
        assertEquals(store.getStoreId(), result.getStoreId());
        assertEquals(store.getStoreName(), result.getStoreName());
        assertEquals("Sniper Kudus 69, One shot one kill", result.getStoreDescription());
        assertEquals(user, result.getUser());
        
        verify(storeRepository, times(2)).save(any(Store.class));
        verify(storeRepository, times(1)).findById(eq(store.getStoreId()));
        verify(userRepository, times(2)).findById(eq(user.getUserId()));
    }

    @Test
    public void testSaveThenPartiallyUpdateStore() {
        User user = userHelper.createModel(1);
        Store store = storeHelper.createModel(1);
        store.setUser(user);
        
        when(storeRepository.save(any(Store.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        
        Store savedStore = storeService.saveStore(store);
        
        assertNotNull(savedStore);
        assertEquals(store.getStoreId(), savedStore.getStoreId());
        assertEquals(store.getStoreName(), savedStore.getStoreName());
        assertEquals(store.getStoreDescription(), savedStore.getStoreDescription());
        assertEquals(user, savedStore.getUser());

        when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));

        Map<String, Object> updatedStore = Map.of(

            "storeDescription","Sniper Kudus 69, One shot one kill"
        );
        
        Store result = storeService.partialUpdateStore(store.getStoreId(), updatedStore);
        
        assertNotNull(result);
        assertEquals(store.getStoreId(), result.getStoreId());
        assertEquals(store.getStoreName(), result.getStoreName());
        assertEquals("Sniper Kudus 69, One shot one kill", result.getStoreDescription());
        assertEquals(user, result.getUser());
        
        verify(storeRepository, times(2)).save(any(Store.class));
        verify(storeRepository, times(1)).findById(eq(store.getStoreId()));
        verify(userRepository, times(1)).findById(eq(user.getUserId()));
    }


}
