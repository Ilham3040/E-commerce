package com.example.shoppingapi;

import com.example.shoppingapi.dto.create.UserCreateDTO;
import com.example.shoppingapi.dto.patch.UserPatchDTO;
import com.example.shoppingapi.dto.put.UserPutDTO;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.UserRepository;
import com.example.shoppingapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;


import java.util.List;
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
        User newUser = userHelper.createModel(1);
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername(newUser.getUsername());
        userCreateDTO.setEmail(newUser.getEmail());
        userCreateDTO.setPhoneNumber(newUser.getPhoneNumber());

        User userToCreate = new User();
        userToCreate.setUsername(userCreateDTO.getUsername());
        userToCreate.setEmail(userCreateDTO.getEmail());
        userToCreate.setPhoneNumber(userCreateDTO.getPhoneNumber());

        when(userRepository.save(any())).thenReturn(userToCreate);

        User createdUser = userService.createUser(userCreateDTO);

        // Assertions
        assertEquals(userToCreate, createdUser);
        verify(userRepository).save(userToCreate);
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
    void updateUser_notFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("updatedAlice");
        userPutDTO.setEmail("updated@example.com");
        userPutDTO.setPhoneNumber("0987654321");

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> userService.updateUser(1L, userPutDTO)
        );
        assertEquals("User not found with ID: 1", ex.getMessage());
        verify(userRepository).findById(1L);
    }

    @Test
    void updateUser_existing_savesUpdatedUser() {
        User existingUser = userHelper.createModel(1);
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername(existingUser.getUsername());
        userPutDTO.setEmail(existingUser.getEmail());
        userPutDTO.setPhoneNumber("0987654321");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any())).thenReturn(existingUser);

        User updatedUser = userService.updateUser(1L, userPutDTO);

        assertEquals(existingUser.getUsername(), updatedUser.getUsername());
        assertEquals(existingUser.getEmail(), updatedUser.getEmail());
        assertEquals("0987654321", updatedUser.getPhoneNumber());
        verify(userRepository).findById(1L);
        verify(userRepository).save(existingUser);
    }

    @Test
    void partialUpdateUser_existing_appliesUpdates() {
        User existingUser = userHelper.createModel(1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        UserPatchDTO userPatchDTO = new UserPatchDTO();
        userPatchDTO.setEmail("updated@example.com");
        userPatchDTO.setPhoneNumber("0987654321");

        User updatedUser = userService.partialUpdateUser(1L, userPatchDTO);

        assertEquals(existingUser.getUsername(), updatedUser.getUsername());
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals("0987654321", updatedUser.getPhoneNumber());
        assertEquals(existingUser.getUsername(), updatedUser.getUsername());

        verify(userRepository).findById(1L);
        verify(userRepository).save(existingUser);
    }

    @Test
    void deleteById_existing_deletesUser() {
        User existingUser = userHelper.createModel(1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        doNothing().when(userRepository).delete(any(User.class));

        userService.deleteById(1L);

        verify(userRepository).findById(1L);
        verify(userRepository).delete(existingUser);
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
