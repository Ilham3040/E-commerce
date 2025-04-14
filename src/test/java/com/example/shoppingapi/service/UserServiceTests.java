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

    private ModelHelper<User> userHelper =  ModelHelperFactory.getModelHelper(User.class);

    @Test
    public void testGetAllUsers() {
        User user1 = userHelper.createModel(1);
        User user2 = userHelper.createModel(1);
    
        List<User> mockUsers = Arrays.asList(user1, user2);
    
        when(userRepository.findAll()).thenReturn(mockUsers);
    
        List<User> result = userService.getAllUsers();
    
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(user1.getUsername(), result.get(0).getUsername());
        assertEquals(user2.getUsername(), result.get(1).getUsername());
    
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testCreateUser() {
        User user = userHelper.createModel(1);

        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals(user.getUsername(), createdUser.getUsername());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getPhoneNumber(), createdUser.getPhoneNumber());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testGetUserById() {
        User user = userHelper.createModel(1);
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(user.getUserId());

        assertTrue(result.isPresent());
        assertEquals(user.getUsername(), result.get().getUsername());
        assertEquals(user.getEmail(), result.get().getEmail());
        assertEquals(user.getPhoneNumber(), result.get().getPhoneNumber());

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
    public void testSaveThenUpdateUser() {
        User user = userHelper.createModel(1);
        
        when(userRepository.save(any(User.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        User savedUser = userService.createUser(user);
        assertNotNull(savedUser);
        assertEquals(user.getUserId(), savedUser.getUserId());
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getPhoneNumber(), savedUser.getPhoneNumber());

        when(userRepository.existsById(eq(user.getUserId()))).thenReturn(true);
        
        User updatedUser = savedUser.toBuilder().phoneNumber("0987654321").build();
        
        User result = userService.updateUser(updatedUser);
        
        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals("0987654321", result.getPhoneNumber());
        
        verify(userRepository, times(2)).save(any(User.class));
        verify(userRepository, times(1)).existsById(eq(user.getUserId()));
    }


    @Test
    public void testSaveThenPartiallyUpdateUser() {
        User user = userHelper.createModel(1);
        
        when(userRepository.save(any(User.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        User savedUser = userService.createUser(user);
        assertNotNull(savedUser);
        assertEquals(user.getUserId(), savedUser.getUserId());
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getPhoneNumber(), savedUser.getPhoneNumber());

        when(userRepository.findById(savedUser.getUserId())).thenReturn(Optional.of(savedUser));
        

        Map<String, Object> updatedUser = Map.of(

            "email", "heyguys@example.com",
            "phoneNumber","0987654321"
        );
        
        User result = userService.partialUpdateUser(savedUser.getUserId(),updatedUser);
        
        assertNotNull(result);
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals( "heyguys@example.com", savedUser.getEmail());
        assertEquals("0987654321", result.getPhoneNumber());
        
        verify(userRepository, times(2)).save(any(User.class));
        verify(userRepository, times(1)).findById(user.getUserId());
    }

}
