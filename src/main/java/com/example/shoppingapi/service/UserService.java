package com.example.shoppingapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.shoppingapi.repository.UserRepository;
import com.example.shoppingapi.model.User;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(Long id, User user) {
        if (!id.equals(user.getUserId())) {
            throw new IllegalArgumentException("User ID in URL and body must match.");
        }
    
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
    
        User updatedUser = user;
        updatedUser.setUserId(id);
    
        return userRepository.save(updatedUser);
    }
    

    public User partialUpdateUser(Long id, Map<String, Object> updates) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(User.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, existingUser, value);
            }
        });

        existingUser.setUserId(id);
        return userRepository.save(existingUser);
    }

    public User deleteById(Long id) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        existingUser.setDeletedAt(LocalDateTime.now());
        return userRepository.save(existingUser);
    }
}
