package com.example.shoppingapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.shoppingapi.service.UserService;
import com.example.shoppingapi.dto.response.UserDTO;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.dto.response.ApiResponse;


import java.util.Optional;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllProductDetails() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }


    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        UserDTO userDTO = new UserDTO(savedUser.getUserId());
        ApiResponse<UserDTO> response = new ApiResponse<>("User successfully added", userDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
        @PathVariable Long id,
        @RequestBody User user) {
        if (user.getEmail() == null || user.getPhoneNumber() == null) {
            ApiResponse<UserDTO> response = new ApiResponse<>("Email and Phone Number are required", null);
            return ResponseEntity.badRequest().body(response);
        }
    
        if (!id.equals(user.getUserId())) {
            ApiResponse<UserDTO> response = new ApiResponse<>("User ID in URL and body must match.", null);
            return ResponseEntity.badRequest().body(response);
        }
    
        user.setUserId(id);
        User updatedUser = userService.updateUser(id, user);
        UserDTO userDTO = new UserDTO(updatedUser.getUserId());
        ApiResponse<UserDTO> response = new ApiResponse<>("User successfully updated", userDTO);
        return ResponseEntity.ok(response);
    }
    

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> partialUpdateUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        User updatedUser = userService.partialUpdateUser(id,updates);
        UserDTO userDTO = new UserDTO(updatedUser.getUserId());
        ApiResponse<UserDTO> response = new ApiResponse<>("User successfully updated", userDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }
}
