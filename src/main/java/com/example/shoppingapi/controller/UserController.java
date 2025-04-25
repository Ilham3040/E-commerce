package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.create.UserCreateDTO;
import com.example.shoppingapi.dto.put.UserPutDTO;
import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.dto.response.UserDTO;
import com.example.shoppingapi.dto.patch.UserPatchDTO;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        return new ApiResponse<>("Fetched user", user, HttpStatus.OK);
    }

    @PostMapping
    public ApiResponse<?> createUser(@Validated @RequestBody UserCreateDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return new ApiResponse<>(errorMessage, null, HttpStatus.BAD_REQUEST);
        }

        User createdUser = userService.createUser(dto);
        return new ApiResponse<>("Successfully created user", createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ApiResponse<UserDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserPutDTO dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return new ApiResponse<>(errorMessage, null, HttpStatus.BAD_REQUEST);
        }

        User updated = userService.updateUser(id, dto);
        return new ApiResponse<>("User updated", new UserDTO(updated.getUserId()), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ApiResponse<UserDTO> partialUpdateUser(
            @PathVariable Long id,
            @RequestBody UserPatchDTO userPatchDTO
    ) {
        User updatedUser = userService.partialUpdateUser(id, userPatchDTO);
        return new ApiResponse<>("User partially updated", new UserDTO(updatedUser.getUserId()), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return new ApiResponse<>("User deleted", null, HttpStatus.NO_CONTENT);
    }
}
