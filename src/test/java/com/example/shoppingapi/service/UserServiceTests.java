package com.example.shoppingapi.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import com.example.shoppingapi.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = UserServiceTests.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final Long userId1 = 1L;
    private final String username1 = "JohnDoe";
    private final String useremail1 = "johndoe@mymail.com";
    private final String usernumber1 = "0888888888888";

    private final Long userId2 = 2L;
    private final String username2 = "Jonathan";
    private final String useremail2 = "jonathan@mymail.com";
    private final String usernumber2 = "0888888";


    @Test
    public void testGetAllUsers() {
        
        User user1 = new User();
        user1.setUserId(userId1);
        user1.setUsername(username1);
        user1.setEmail(useremail1);
        user1.setPhoneNumber(usernumber1);
    
        User user2 = new User();
        user2.setUserId(userId2);
        user2.setUsername(username2);
        user2.setEmail(useremail2);
        user2.setPhoneNumber(usernumber2);
    
        List<User> mockUsers = Arrays.asList(user1, user2);
    
        when(userRepository.findAll()).thenReturn(mockUsers);
    
        
        List<User> result = userService.getAllUsers();
    
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(username1, result.get(0).getUsername());
        assertEquals(username2, result.get(1).getUsername());
    
        verify(userRepository, times(1)).findAll();
    }
    


    @Test
    public void testCreateUser() {
        User user = new User();
        user.setUsername(username1);
        user.setEmail(useremail1);
        user.setPhoneNumber(usernumber1);

        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals(username1, createdUser.getUsername());
        assertEquals(useremail1, createdUser.getEmail());
        assertEquals(usernumber1, createdUser.getPhoneNumber());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testGetUserById() {
    User user = new User();
    user.setUserId(userId1);
    user.setUsername(username1);
    user.setEmail(useremail1);
    user.setPhoneNumber(usernumber1);

    when(userRepository.findById(userId1)).thenReturn(Optional.of(user));

    Optional<User> result = userService.getUserById(userId1);

    assertTrue(result.isPresent());
    assertEquals(username1, result.get().getUsername());
    assertEquals(useremail1, result.get().getEmail());
    assertEquals(usernumber1, result.get().getPhoneNumber());

    verify(userRepository, times(1)).findById(userId1);
    }


    @Test
    public void testGetUserById_NotFound() {
    when(userRepository.findById(userId1)).thenReturn(Optional.empty());

    Optional<User> result = userService.getUserById(userId1);

    assertFalse(result.isPresent());
    verify(userRepository, times(1)).findById(userId1);
    }

    @Test
    public void testUpdateUser() {
        User existingUser = new User();
        existingUser.setUserId(userId1);
        existingUser.setUsername(username1);
        existingUser.setEmail(useremail1);
        existingUser.setPhoneNumber(usernumber1);

        when(userRepository.findById(userId1)).thenReturn(Optional.of(existingUser));
        verify(userRepository).findById(userId1);
    
        User updatedUser = new User();
        updatedUser.setUserId(userId1);
        updatedUser.setUsername(username2);
        updatedUser.setEmail(useremail2);
        updatedUser.setPhoneNumber("0987654321");

        
        when(userRepository.save(any(User.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        User result = userService.updateUser(updatedUser);
        assertNotNull(result);
        assertEquals(username2, result.getUsername());
        assertEquals(useremail2, result.getEmail());
        assertEquals("0987654321", result.getPhoneNumber());
    
        verify(userRepository, times(1)).findById(userId1);
        verify(userRepository, times(1)).save(existingUser); 
    }



}
