package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.UserFavoriteCreateDTO;
import com.example.shoppingapi.model.*;
import com.example.shoppingapi.repository.UserFavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFavoriteService {

    private final UserFavoriteRepository userFavoriteRepository;

    // Method to return products for a given user ID
    public List<UserFavorite> getAllByUserId(Long id) {
        return userFavoriteRepository.findItemsByUserId(id);
    }

    public UserFavorite findById(UserFavoriteId id) {
        return userFavoriteRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("UserFavorite not found with ID: " + id));
    }

    public UserFavorite addingUserFavorite(UserFavoriteCreateDTO userFavoriteCreateDTO) {
        UserFavoriteId userFavoriteId = UserFavoriteId.builder().userId(userFavoriteCreateDTO.getUserId()).productId(userFavoriteCreateDTO.getProductId()).build();

        UserFavorite userFavorite = UserFavorite.builder()
                .id(userFavoriteId)
                .user(User.builder().userId(userFavoriteCreateDTO.getUserId()).build())
                .product(Product.builder().productId(userFavoriteCreateDTO.getProductId()).build())
                .build();

        return userFavoriteRepository.save(userFavorite);
    }

    public void deleteById(UserFavoriteId id) {
        findById(id);
        userFavoriteRepository.deleteById(id);
    }
}
