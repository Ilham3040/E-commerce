package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.create.UserFavoriteCreateDTO;
import com.example.shoppingapi.dto.request.UserFavoriteCreateDTO;
import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.dto.response.ProductDTO;
import com.example.shoppingapi.dto.response.UserFavoriteDTO;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.UserFavorite;
import com.example.shoppingapi.model.UserFavoriteId;
import com.example.shoppingapi.service.UserFavoriteService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/userfavorites")
@RequiredArgsConstructor
public class UserFavoriteController {
    private final UserFavoriteService userFavoriteService;

    @GetMapping("/user/{userId}")
    public ApiResponse<List<ProductDTO>> getAllUserFavoritesByUserId(@PathVariable Long userId) {
        List<ProductDTO> productDTOs = userFavoriteService.getAllByUserId(userId)
                .stream()
                .map(cart -> new ProductDTO(cart.getProductId(),cart.getStore().getStoreId()))
                .collect(Collectors.toList());
        return new ApiResponse<>("Successfully fetched all user carts", productDTOs, HttpStatus.OK);
    }

    @PostMapping
    public ApiResponse<UserFavoriteDTO> addToUserFavorites(@RequestBody UserFavoriteCreateDTO userFavoriteCreateDTO) {
        UserFavorite addedFavorite = userFavoriteService.addingUserFavorite(userFavoriteCreateDTO);
        return new ApiResponse<>("Successfully added product to user favorites", new UserFavoriteDTO(addedFavorite.getFavoriteId(), addedFavorite.getProduct().getProductId()), HttpStatus.CREATED);
    }

    @DeleteMapping("/{favoriteId}")
    public ApiResponse<Void> removeFromUserFavorites(@PathVariable UserFavoriteId favoriteId) {
        userFavoriteService.deleteById(favoriteId);
        return new ApiResponse<>("Successfully removed product from user favorites", null, HttpStatus.NO_CONTENT);
    }
}
