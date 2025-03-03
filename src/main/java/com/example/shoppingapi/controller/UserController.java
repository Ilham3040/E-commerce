package com.example.shoppingapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.shoppingapi.service.UserService;
import com.example.shoppingapi.dto.UserDTO;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.dto.ApiResponse;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);

        // Convert to DTO before returning
        UserDTO userDTO = new UserDTO(savedUser.getUserId());

        
         ApiResponse<UserDTO> response = new ApiResponse<>("User successfully added", userDTO);
         return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }
}
