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

    private final Long userId1 = 1L;
    private final String username1 = "JohnDoe";
    private final String useremail1 = "johndoe@mymail.com";
    private final String usernumber1 = "0888888888888";

    private final Long storeId1 = 1L;
    private final String storename1 = "Pet shop";
    private final String storedesc1 = "El yapping";

    private final Long userId2 = 2L;
    private final String username2 = "Jonathan";
    private final String useremail2 = "jonathan@mymail.com";
    private final String usernumber2 = "0888888";

    private final Long storeId2 = 2L;
    private final String storename2 = "Book store";
    private final String storedesc2 = "(.... aja sendiri)";


    @Test
    public void testGetAllStore() {
                
        User user1 = new User();
        user1.setUserId(userId1);
        user1.setUsername(username1);
        user1.setEmail(useremail1);
        user1.setPhoneNumber(usernumber1);

        Store store1 = new Store();
        store1.setStoreId(storeId1);
        store1.setStoreName(storename1);
        store1.setStoreDescription(storedesc1);
        store1.setUser(user1);

        User user2 = new User();
        user2.setUserId(userId2);
        user2.setUsername(username2);
        user2.setEmail(useremail2);
        user2.setPhoneNumber(usernumber2);

        Store store2 = new Store();
        store2.setStoreId(storeId2);
        store2.setStoreName(storename2);
        store2.setStoreDescription(storedesc2);
        store2.setUser(user2);


        List<Store> mockStores = Arrays.asList(store1,store2);

        when(storeRepository.findAll()).thenReturn(mockStores);

        List<Store> result = storeService.getAllStores();

                
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(storeId1, result.get(0).getStoreId());
        assertEquals(storename1, result.get(0).getStoreName());
        assertEquals(storedesc1, result.get(0).getStoreDescription());
        assertEquals(user1, result.get(0).getUser());
        assertEquals(storeId2, result.get(1).getStoreId());
        assertEquals(storename2, result.get(1).getStoreName());
        assertEquals(storedesc2, result.get(1).getStoreDescription());
        assertEquals(user2, result.get(1).getUser());

        verify(storeRepository, times(1)).findAll();

    }

    @Test
    public void testGetStore(){
        User user = new User();
        user.setUserId(userId1);
        user.setUsername(username1);
        user.setEmail(useremail1);
        user.setPhoneNumber(usernumber1);

        Store store = new Store();
        store.setStoreId(storeId1);
        store.setStoreName(storename1);
        store.setStoreDescription(storedesc1);
        store.setUser(user);

        when(storeRepository.findById(storeId1)).thenReturn(Optional.of(store));

        Optional<Store> result = storeService.getStoreById(storeId1);

        assertTrue(result.isPresent());
        assertEquals(storeId1, result.get().getStoreId());
        assertEquals(storename1, result.get().getStoreName());
        assertEquals(storedesc1, result.get().getStoreDescription());
        assertEquals(user, result.get().getUser());

        verify(storeRepository, times(1)).findById(storeId1);


    }


    @Test
    public void testGetStorerById_NotFound() {
        when(storeRepository.findById(storeId1)).thenReturn(Optional.empty());

        Optional<Store> result = storeService.getStoreById(storeId1);

        assertFalse(result.isPresent());
        verify(storeRepository, times(1)).findById(storeId1);
    }

    @Test
    public void testCreateStore() {
        User user = new User();
        user.setUserId(userId1);
        user.setUsername(username1);
        user.setEmail(useremail1);
        user.setPhoneNumber(usernumber1);

        Store store = new Store();
        store.setStoreId(storeId1);
        store.setStoreName(storename1);
        store.setStoreDescription(storedesc1);
        store.setUser(user);

        when(storeRepository.save(any(Store.class))).thenReturn(store);
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));

        Store createdStore = storeService.saveStore(store);

        assertNotNull(createdStore);
        assertEquals(storeId1, createdStore.getStoreId());
        assertEquals(storename1, createdStore.getStoreName());
        assertEquals(storedesc1, createdStore.getStoreDescription());
        assertEquals(user, createdStore.getUser());


        verify(userRepository, times(1)).findById(userId1);
        verify(storeRepository, times(1)).save(store);
    }


    @Test
    public void testSaveThenUpdateUser() {
        User user = new User();
        user.setUserId(userId1);
        user.setUsername(username1);
        user.setEmail(useremail1);
        user.setPhoneNumber(usernumber1);

        Store store = new Store();
        store.setStoreId(storeId1);
        store.setStoreName(storename1);
        store.setStoreDescription(storedesc1);
        store.setUser(user);
        
        when(storeRepository.save(any(Store.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        
        Store savedStore = storeService.saveStore(store);
        
        assertNotNull(savedStore);
        assertEquals(storeId1, savedStore.getStoreId());
        assertEquals(storename1, savedStore.getStoreName());
        assertEquals(storedesc1, savedStore.getStoreDescription());
        assertEquals(user, savedStore.getUser());

        when(storeRepository.findById(storeId1)).thenReturn(Optional.of(store));
        
        Store updatedStore = new Store();
        updatedStore.setStoreId(storeId1);
        updatedStore.setStoreName(storename1);
        updatedStore.setStoreDescription("Sniper Kudus 69, One shot one kill");
        updatedStore.setUser(user);
        
        Store result = storeService.updateStore(storeId1, updatedStore);
        
        assertNotNull(result);
        assertEquals(storeId1, result.getStoreId());
        assertEquals(storename1, result.getStoreName());
        assertEquals("Sniper Kudus 69, One shot one kill", result.getStoreDescription());
        assertEquals(user, result.getUser());
        
        verify(storeRepository, times(2)).save(any(Store.class));
        verify(storeRepository, times(1)).findById(eq(storeId1));
        verify(userRepository, times(2)).findById(eq(userId1));
    }

    @Test
    public void testSaveThenPartiallyUpdateStore() {
        User user = new User();
        user.setUserId(userId1);
        user.setUsername(username1);
        user.setEmail(useremail1);
        user.setPhoneNumber(usernumber1);

        Store store = new Store();
        store.setStoreId(storeId1);
        store.setStoreName(storename1);
        store.setStoreDescription(storedesc1);
        store.setUser(user);
        
        when(storeRepository.save(any(Store.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        
        Store savedStore = storeService.saveStore(store);
        
        assertNotNull(savedStore);
        assertEquals(storeId1, savedStore.getStoreId());
        assertEquals(storename1, savedStore.getStoreName());
        assertEquals(storedesc1, savedStore.getStoreDescription());
        assertEquals(user, savedStore.getUser());

        when(storeRepository.findById(storeId1)).thenReturn(Optional.of(store));
        

        Map<String, Object> updatedStore = Map.of(

            "storeDescription","Sniper Kudus 69, One shot one kill"
        );
        
        Store result = storeService.partialUpdateStore(storeId1, updatedStore);
        
        assertNotNull(result);
        assertEquals(storeId1, result.getStoreId());
        assertEquals(storename1, result.getStoreName());
        assertEquals("Sniper Kudus 69, One shot one kill", result.getStoreDescription());
        assertEquals(user, result.getUser());
        
        verify(storeRepository, times(2)).save(any(Store.class));
        verify(storeRepository, times(1)).findById(eq(storeId1));
        verify(userRepository, times(1)).findById(eq(userId1));
    }


}
