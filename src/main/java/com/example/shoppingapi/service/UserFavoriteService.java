package com.example.shoppingapi.service;

import com.example.shoppingapi.model.UserFavorite;
import com.example.shoppingapi.model.UserFavoriteId;
import com.example.shoppingapi.repository.UserFavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserFavoriteService {

    @Autowired
    private UserFavoriteRepository userFavoriteRepository;

    public List<UserFavorite> findAll() {
        return userFavoriteRepository.findAll();
    }

    public Optional<UserFavorite> findById(Long userId, Long productId) {
        UserFavoriteId id = new UserFavoriteId();
        id.setUserId(userId);
        id.setProductId(productId);
        return userFavoriteRepository.findById(id);
    }

    public UserFavorite saveUserFavorite(UserFavorite userFavorite) {
        return userFavoriteRepository.save(userFavorite);
    }

    public void deleteById(UserFavoriteId id) {
    if (!userFavoriteRepository.existsById(id)) {
        throw new ResourceNotFoundException("UserFavorite not found with ID: " + id);
    }
    userFavoriteRepository.deleteById(id);
    }
}
