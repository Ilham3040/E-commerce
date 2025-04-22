package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.request.UserRequestDTO;
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

    public User createUser(UserRequestDTO userRequestDTO) {
        User user = User.builder()
                .username(userRequestDTO.getUsername())
                .email(userRequestDTO.getEmail())
                .phoneNumber(userRequestDTO.getPhoneNumber())
                .build();
        return userRepository.save(user);
    }

    public User updateUser(Long id, UserRequestDTO userRequestDTO) {
        User existingUser = getUserById(id);
        existingUser.setUsername(userRequestDTO.getUsername());
        existingUser.setEmail(userRequestDTO.getEmail());
        existingUser.setPhoneNumber(userRequestDTO.getPhoneNumber());
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
