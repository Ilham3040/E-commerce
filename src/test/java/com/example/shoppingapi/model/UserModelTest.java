package com.example.shoppingapi.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class UserModelTest {

    @Test
    public void testPrePersistSetsTimestamps() {
        User user = User.builder().build();
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());

        user.prePersist();

        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());

        LocalDateTime now = LocalDateTime.now();
        assertTrue(!user.getCreatedAt().isAfter(now));
        assertTrue(!user.getUpdatedAt().isAfter(now));
    }

    @Test
    public void testPreUpdateModifiesUpdatedAt() throws InterruptedException {
        User user = User.builder().build();
        user.prePersist();
        LocalDateTime initialUpdatedAt = user.getUpdatedAt();

        Thread.sleep(50);

        user.preUpdate();
        LocalDateTime updatedUpdatedAt = user.getUpdatedAt();

        assertNotNull(updatedUpdatedAt);
        assertTrue(updatedUpdatedAt.isAfter(initialUpdatedAt));
    }

    @Test
    public void testPreRemoveSetsDeletedAt() {
        User user = User.builder().build();

        assertNull(user.getDeletedAt());

        user.preRemove();

        assertNotNull(user.getDeletedAt());
        
        LocalDateTime now = LocalDateTime.now();
        assertTrue(!user.getDeletedAt().isAfter(now));
    }

    @Test
    public void testGetterAndSetter() {
        User user = User.builder().build();
        String username = "testuser";
        String email = "testuser@example.com";
        String phone = "1234567890";

        user.setUsername(username);
        user.setEmail(email);
        user.setPhoneNumber(phone);

        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(phone, user.getPhoneNumber());
    }
}
