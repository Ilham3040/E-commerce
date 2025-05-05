package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.create.UserCartCreateDTO;
import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.dto.response.ProductDTO;
import com.example.shoppingapi.dto.response.UserCartDTO;
import com.example.shoppingapi.model.UserCart;
import com.example.shoppingapi.model.UserCartId;
import com.example.shoppingapi.service.UserCartService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usercart/")
@RequiredArgsConstructor
public class UserCartController {
    private final UserCartService userCartService;

    @GetMapping("/user/{userId}")
    public ApiResponse<List<UserCartDTO>> getAllUserCartsByUserId(@PathVariable Long userId) {
        List<UserCartDTO> userCartDTOs = userCartService.getAllByUserId(userId)
                .stream()
                .map(cart -> new UserCartDTO(cart.getUser().getUserId(), cart.getProduct().getProductId(),cart.getQuantity()))
                .collect(Collectors.toList());
        return new ApiResponse<>("Successfully fetched all user carts", userCartDTOs, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<UserCartDTO> addToUserCart(@RequestBody UserCartCreateDTO userCartCreateDTO) {
        UserCart addedCart = userCartService.addingUserCart(userCartCreateDTO);
        return new ApiResponse<>("Successfully added product to user cart", new UserCartDTO(addedCart.getUser().getUserId(), addedCart.getProduct().getProductId(),addedCart.getQuantity()), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}/{productId}")
    public ApiResponse<Void> removeFromUserCart(@PathVariable Long userId, @PathVariable Long productId) {
        UserCartId cartId = UserCartId.builder().userId(userId).productId(productId).build();
        userCartService.deleteById(cartId);
        return new ApiResponse<>("Successfully removed product from user cart", null, HttpStatus.NO_CONTENT);
    }
}
