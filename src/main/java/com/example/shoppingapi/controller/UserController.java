package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.request.UserRequestDTO;
import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.dto.response.UserDTO;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ApiResponse<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers()
            .stream()
            .map(u -> new UserDTO(u.getUserId()))
            .collect(Collectors.toList());
        return new ApiResponse<>("Fetched all users", users);
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDTO> getUserById(@PathVariable Long id) {
        User u = userService.getUserById(id);
        return new ApiResponse<>("Fetched user", new UserDTO(u.getUserId()));
    }
    

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserDTO> createUser(@Validated @RequestBody UserRequestDTO dto) {
        User toCreate = User.builder()
            .username(dto.getUsername())
            .email(dto.getEmail())
            .phoneNumber(dto.getPhoneNumber())
            .build();
        User created = userService.createUser(toCreate);
        return new ApiResponse<>("User created", new UserDTO(created.getUserId()));
    }

    @PutMapping("/{id}")
    public ApiResponse<UserDTO> updateUser(
        @PathVariable Long id,
        @Validated @RequestBody UserRequestDTO dto
    ) {
        User toUpdate = User.builder()
            .userId(id)
            .username(dto.getUsername())
            .email(dto.getEmail())
            .phoneNumber(dto.getPhoneNumber())
            .build();
        User updated = userService.updateUser(id, toUpdate);
        return new ApiResponse<>("User updated", new UserDTO(updated.getUserId()));
    }

    @PatchMapping("/{id}")
    public ApiResponse<UserDTO> partialUpdateUser(
        @PathVariable Long id,
        @RequestBody Map<String,Object> updates
    ) {
        User updated = userService.partialUpdateUser(id, updates);
        return new ApiResponse<>("User partially updated", new UserDTO(updated.getUserId()));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return new ApiResponse<>("User deleted", null);
    }
}
