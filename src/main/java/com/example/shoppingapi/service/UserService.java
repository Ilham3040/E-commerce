package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.UserCreateDTO;
import com.example.shoppingapi.dto.patch.UserPatchDTO;
import com.example.shoppingapi.dto.put.UserPutDTO;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.UserRepository;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("User not found with ID: " + id));
    }

    public User getUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username))
            .orElseThrow(() ->
                new ResourceNotFoundException("User not found with username: " + username));
    }

    public User createUser(UserCreateDTO userCreateDTO) {
        User user = User.builder()
                .username(userCreateDTO.getUsername())
                .email(userCreateDTO.getEmail())
                .phoneNumber(userCreateDTO.getPhoneNumber())
                .build();
        return userRepository.save(user);
    }

    public User updateUser(Long id,UserPutDTO userPutDTO) {
        User existingUser = getUserById(id);
        ReflectionUtils.doWithFields(UserPutDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(userPutDTO);

            if (value != null) {
                Field userField = ReflectionUtils.findField(User.class, field.getName());
                if (userField != null) {
                    userField.setAccessible(true);
                    userField.set(existingUser, value);
                }
            }
        });

        return userRepository.save(existingUser);
    }


    public User partialUpdateUser(Long id, UserPatchDTO userPatchDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ReflectionUtils.doWithFields(UserPatchDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(userPatchDTO);
            if (value != null) {
                Field userField = ReflectionUtils.findField(User.class, field.getName());
                if (userField != null) {
                    userField.setAccessible(true);
                    userField.set(existingUser, value);
                }
            }
        });

        return userRepository.save(existingUser);
    }


    public User deleteById(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
        return user;
    }

}
