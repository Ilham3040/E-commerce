package com.example.shoppingapi.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class StoreModelTest {

    @Test
    public void testPrePersistSetsTimestamps() {
        Store store = Store.builder().build();
        assertNull(store.getCreatedAt());
        assertNull(store.getUpdatedAt());

        store.prePersist();

        assertNotNull(store.getCreatedAt());
        assertNotNull(store.getUpdatedAt());

        LocalDateTime now = LocalDateTime.now();
        assertTrue(!store.getCreatedAt().isAfter(now));
        assertTrue(!store.getUpdatedAt().isAfter(now));
    }

    @Test
    public void testPreUpdateModifiesUpdatedAt() throws InterruptedException {
        Store store = Store.builder().build();
        store.prePersist();
        LocalDateTime initialUpdatedAt = store.getUpdatedAt();

        Thread.sleep(50);

        store.preUpdate();
        LocalDateTime updatedUpdatedAt = store.getUpdatedAt();

        assertNotNull(updatedUpdatedAt);
        assertTrue(updatedUpdatedAt.isAfter(initialUpdatedAt));
    }

    @Test
    public void testPreRemoveSetsDeletedAt() {
        Store store = Store.builder().build();

        assertNull(store.getDeletedAt());

        store.preRemove();

        assertNotNull(store.getDeletedAt());
        
        LocalDateTime now = LocalDateTime.now();
        assertTrue(!store.getDeletedAt().isAfter(now));
    }

    @Test
    public void testGetterAndSetter() {
        User user = User.builder()
            .username("testuser")
            .email("testuser@example.com")
            .phoneNumber("1234567890")
            .build();
    
        Store store = Store.builder()
            .storeName("userstore")
            .user(user)
            .build();
        
        assertEquals("userstore", store.getStoreName());
        assertEquals("testuser", store.getUser().getUsername());
        assertEquals("testuser@example.com", store.getUser().getEmail());
        assertEquals("1234567890", store.getUser().getPhoneNumber());
        
    
    }
}
