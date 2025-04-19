package com.example.shoppingapi.service;

import com.example.shoppingapi.model.User;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final ModelHelper<User> userHelper =
        ModelHelperFactory.getModelHelper(User.class);

    @Test
    void getAllUsers_returnsAllUsers() {
        List<User> expected = List.of(
            userHelper.createModel(1),
            userHelper.createModel(2)
        );
        when(userRepository.findAll()).thenReturn(expected);

        List<User> actual = userService.getAllUsers();

        assertEquals(expected, actual);
        verify(userRepository).findAll();
    }

    @Test
    void createUser_savesAndReturnsUser() {
        User toCreate = userHelper.createModel(1);
        when(userRepository.save(any())).thenReturn(toCreate);

        User created = userService.createUser(toCreate);

        assertEquals(toCreate, created);
        verify(userRepository).save(toCreate);
    }

    @Test
    void getUserById_found_returnsUser() {
        User expected = userHelper.createModel(1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(expected));

        User actual = userService.getUserById(1L);

        assertEquals(expected, actual);
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_notFound_throwsException() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> userService.getUserById(2L)
        );
        assertEquals("User not found with ID: 2", ex.getMessage());
        verify(userRepository).findById(2L);
    }

    @Test
    void getUserByUsername_found_returnsUser() {
        User expected = userHelper.createModel(1);
        when(userRepository.findByUsername("alice"))
            .thenReturn(expected);

        User actual = userService.getUserByUsername("alice");

        assertEquals(expected, actual);
        verify(userRepository).findByUsername("alice");
    }

    @Test
    void getUserByUsername_notFound_throwsException() {
        when(userRepository.findByUsername("bob"))
            .thenReturn(null);

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> userService.getUserByUsername("bob")
        );
        assertEquals("User not found with username: bob", ex.getMessage());
        verify(userRepository).findByUsername("bob");
    }

    @Test
    void updateUser_idMismatch_throwsException() {
        User user = userHelper.createModel(1);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> userService.updateUser(2L, user)
        );
        assertEquals("User ID in URL and body must match.", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_notFound_throwsException() {
        User user = userHelper.createModel(1);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> userService.updateUser(1L, user)
        );
        assertEquals("User not found with ID: 1", ex.getMessage());
        verify(userRepository).findById(1L);
    }

    @Test
    void updateUser_existing_savesUpdatedUser() {
        User original = userHelper.createModel(1);
        User updated  = original.toBuilder()
                                .phoneNumber("9999999999")
                                .build();

        when(userRepository.findById(1L))
            .thenReturn(Optional.of(original));
        when(userRepository.save(any()))
            .thenReturn(updated);

        User result = userService.updateUser(1L, updated);

        assertEquals(updated, result);
        verify(userRepository).findById(1L);
        verify(userRepository).save(updated);
    }

    @Test
    void partialUpdateUser_existing_appliesUpdates() {
        User existing = userHelper.createModel(1);
        when(userRepository.findById(1L))
            .thenReturn(Optional.of(existing));
        when(userRepository.save(any()))
            .thenAnswer(inv -> inv.getArgument(0));

        Map<String, Object> changes = Map.of(
            "email", "new@example.com",
            "phoneNumber", "1112223333"
        );
        User result = userService.partialUpdateUser(1L, changes);

        assertEquals("new@example.com", result.getEmail());
        assertEquals("1112223333",      result.getPhoneNumber());
        verify(userRepository).findById(1L);
        verify(userRepository).save(existing);
    }

    @Test
    void deleteById_existing_setsDeletedAt() {
        User existing = userHelper.createModel(1);
        when(userRepository.findById(1L))
            .thenReturn(Optional.of(existing));
        when(userRepository.save(any()))
            .thenAnswer(inv -> inv.getArgument(0));

        User result = userService.deleteById(1L);

        assertNotNull(result.getDeletedAt());
        assertTrue(result.getDeletedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        verify(userRepository).findById(1L);
        verify(userRepository).save(existing);
    }

    @Test
    void deleteById_notFound_throwsException() {
        when(userRepository.findById(4L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> userService.deleteById(4L)
        );
        assertEquals("User not found with ID: 4", ex.getMessage());
        verify(userRepository).findById(4L);
    }
}
