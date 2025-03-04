package com.example.shoppingapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.shoppingapi.repository.UserRepository;
import com.example.shoppingapi.model.User;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Map;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public User updateUser(User user) {
        
        if (!userRepository.existsById(user.getUserId())) {
            throw new ResourceNotFoundException("User not found with ID: " + user.getUserId());
        }
        
        return userRepository.save(user);
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

        return userRepository.save(existingUser);
    }
}



