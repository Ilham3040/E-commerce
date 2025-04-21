package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.request.UserRequestDTO;
import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.dto.response.UserDTO;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
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
        return new ApiResponse<>("Fetched all users", users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ApiResponse<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return new ApiResponse<>("Fetched user", user , HttpStatus.OK);
    }

    @PostMapping
    public ApiResponse<?> createUser(@Validated @RequestBody UserRequestDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return new ApiResponse<>(errorMessage, null, HttpStatus.BAD_REQUEST);
        }

        User wannabe = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .build();

        User createdUser = userService.createUser(wannabe);
        return new ApiResponse<>("Successfully created user", createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ApiResponse<UserDTO> updateUser(
            @PathVariable Long id,
            @Validated @RequestBody UserRequestDTO dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return new ApiResponse<>(errorMessage, null, HttpStatus.BAD_REQUEST);
        }

        User wannabe = User.builder()
                .userId(id)
                .username(dto.getUsername())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .build();

        User updated = userService.updateUser(id, wannabe);
        return new ApiResponse<>("User updated", new UserDTO(updated.getUserId()), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ApiResponse<UserDTO> partialUpdateUser(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates
    ) {
        User updated = userService.partialUpdateUser(id, updates);
        return new ApiResponse<>("User partially updated", new UserDTO(updated.getUserId()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return new ApiResponse<>("User deleted", null, HttpStatus.NO_CONTENT);
    }
}
