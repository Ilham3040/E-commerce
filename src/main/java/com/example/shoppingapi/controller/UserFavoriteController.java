package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.dto.response.UserFavoriteDTO;
import com.example.shoppingapi.model.UserFavorite;
import com.example.shoppingapi.service.UserFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user-favorites")
public class UserFavoriteController {

    @Autowired
    private UserFavoriteService userFavoriteService;

    @GetMapping
    public List<UserFavorite> getAllUserFavorites() {
        return userFavoriteService.findAll();
    }

    @GetMapping("/{userId}/{productId}")
    public Optional<UserFavorite> getUserFavoriteById(@PathVariable Long userId,
                                                      @PathVariable Long productId) {
        return userFavoriteService.findById(userId, productId);
    }

    @PostMapping
    public ResponseEntity<UserFavoriteDTO> createUserFavorite(@RequestBody UserFavorite userFavorite) {
        UserFavorite savedUserFavorite = userFavoriteService.saveUserFavorite(userFavorite);
        UserFavoriteDTO dto = new UserFavoriteDTO(savedUserFavorite.getId().getUserId(),
                savedUserFavorite.getId().getProductId());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{userId}/{productId}")
    public ResponseEntity<ApiResponse<UserFavoriteDTO>> updateUserFavorite(@PathVariable Long userId,
                                                                           @PathVariable Long productId,
                                                                           @RequestBody UserFavorite userFavorite) {
        try {
            UserFavorite updatedUserFavorite = userFavoriteService.updateUserFavorite(userId, productId, userFavorite);
            UserFavoriteDTO dto = new UserFavoriteDTO(updatedUserFavorite.getId().getUserId(),
                    updatedUserFavorite.getId().getProductId());
            ApiResponse<UserFavoriteDTO> response = new ApiResponse<>("UserFavorite successfully updated", dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<UserFavoriteDTO> response = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PatchMapping("/{userId}/{productId}")
    public ResponseEntity<ApiResponse<UserFavoriteDTO>> partialUpdateUserFavorite(@PathVariable Long userId,
                                                                                @PathVariable Long productId,
                                                                                @RequestBody Map<String, Object> updates) {
        try {
            UserFavorite updatedUserFavorite = userFavoriteService.partialUpdateUserFavorite(userId, productId, updates);
            UserFavoriteDTO dto = new UserFavoriteDTO(updatedUserFavorite.getId().getUserId(),
                    updatedUserFavorite.getId().getProductId());
            ApiResponse<UserFavoriteDTO> response = new ApiResponse<>("UserFavorite successfully updated", dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<UserFavoriteDTO> response = new ApiResponse<>("Error updating UserFavorite: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{userId}/{productId}")
    public void deleteByIdFavorite(@PathVariable Long userId,
                                   @PathVariable Long productId) {
        userFavoriteService.deleteById(userId, productId);
    }
}
