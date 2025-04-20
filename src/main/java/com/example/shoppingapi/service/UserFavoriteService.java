package com.example.shoppingapi.service;

import com.example.shoppingapi.model.UserFavorite;
import com.example.shoppingapi.model.UserFavoriteId;
import com.example.shoppingapi.repository.UserFavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFavoriteService {

    private final UserFavoriteRepository userFavoriteRepository;

    public List<UserFavorite> findAll() {
        return userFavoriteRepository.findAll();
    }

    public UserFavorite findById(Long userId, Long productId) {
        UserFavoriteId id = new UserFavoriteId(userId, productId);
        return userFavoriteRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("UserFavorite not found with ID: " + id));
    }

    public UserFavorite saveUserFavorite(UserFavorite favorite) {
        return userFavoriteRepository.save(favorite);
    }

    public void deleteById(UserFavoriteId id) {
        findById(id.getUserId(), id.getProductId());
        userFavoriteRepository.deleteById(id);
    }
}
