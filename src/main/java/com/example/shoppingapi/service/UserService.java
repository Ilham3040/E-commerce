package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.UserCreateDTO;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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

    public User updateUser(Long id, UserCreateDTO userCreateDTO) {
        User existingUser = getUserById(id);
        existingUser.setUsername(userCreateDTO.getUsername());
        existingUser.setEmail(userCreateDTO.getEmail());
        existingUser.setPhoneNumber(userCreateDTO.getPhoneNumber());
        return userRepository.save(existingUser);
    }


    public User partialUpdateUser(Long id, Map<String, Object> updates) {
        User existing = getUserById(id);
        BeanWrapper wrapper = new BeanWrapperImpl(existing);
        updates.forEach((key, value) -> {
            if (!"userId".equals(key)) {
                wrapper.setPropertyValue(key, value);
            }
        });
        return userRepository.save(existing);
    }

    public User deleteById(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
        return user;
    }

}
