package com.example.shoppingapi.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.example.shoppingapi.model.User;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = UserServiceTests.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final ModelHelper<User> userHelper = ModelHelperFactory.getModelHelper(User.class);

    @Test
    public void testGetAllUsers() {
        User user1 = userHelper.createModel(1);
        User user2 = userHelper.createModel(2);
    
        List<User> mockUsers = Arrays.asList(user1, user2);
    
        when(userRepository.findAll()).thenReturn(mockUsers);
    
        List<User> result = userService.getAllUsers();
    
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mockUsers, result);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testCreateUser() {
        User user = userHelper.createModel(1);

        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals(user, createdUser);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testGetUserById() {
        User user = userHelper.createModel(1);
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        User result = userService.getUserById(user.getUserId()).orElseThrow(() -> new AssertionError("User not found"));

        assertNotNull(result);
        assertEquals(user, result);

        verify(userRepository, times(1)).findById(user.getUserId());
    }

    @Test
    public void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(1L);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(1L);
    }
    @Test
    public void testUpdateUser() {
        User user = userHelper.createModel(1);
        
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = user.toBuilder().phoneNumber("0123456789").build();
        User result = userService.updateUser(user.getUserId(), updatedUser);

        assertNotNull(result);
        assertEquals(updatedUser, result);

        verify(userRepository, times(1)).findById(user.getUserId());
        verify(userRepository, times(1)).save(updatedUser);
    }
    
    

    @Test
    public void testPartialUpdateUser() {
        User existing = userHelper.createModel(1);

        when(userRepository.findById(existing.getUserId())).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, Object> updates = Map.of(
            "email", "heyguys@example.com",
            "phoneNumber", "0987654321"
        );
        User result = userService.partialUpdateUser(existing.getUserId(), updates);

        assertNotNull(result);
        assertEquals("heyguys@example.com", result.getEmail());
        assertEquals("0987654321", result.getPhoneNumber());
        
        verify(userRepository, times(1)).findById(existing.getUserId());
        verify(userRepository, times(1)).save(existing);
    }
    

    @Test
    public void testSoftDeleteById() {
        User user = userHelper.createModel(1);
        
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User deletedUser = userService.deleteById(user.getUserId());

        assertNotNull(deletedUser);
        assertNotNull(deletedUser.getDeletedAt());
        assertEquals(user.getUserId(), deletedUser.getUserId());

        verify(userRepository, times(1)).save(deletedUser);
        verify(userRepository, times(1)).findById(user.getUserId());
    }

    @Test
    public void testSoftDeleteById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            userService.deleteById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals("User not found with ID: 1", e.getMessage());
        }

        verify(userRepository, times(1)).findById(1L);
    }
}
