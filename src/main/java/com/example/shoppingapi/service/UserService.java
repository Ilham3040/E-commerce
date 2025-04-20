package com.example.shoppingapi.service;

import com.example.shoppingapi.model.User;
import com.example.shoppingapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public User createUser(User user) {
        return userRepository.save(user);
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

    public User updateUser(Long id, User user) {
        if (!id.equals(user.getUserId())) {
            throw new IllegalArgumentException("User ID in URL and body must match.");
        }
        return userRepository.findById(id)
            .map(existing -> {
                user.setUserId(id);
                return userRepository.save(user);
            })
            .orElseThrow(() ->
                new ResourceNotFoundException("User not found with ID: " + id));
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
        user.setDeletedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
}
