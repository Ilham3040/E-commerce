package com.example.shoppingapi.service;

import com.example.shoppingapi.model.UserFavorite;
import com.example.shoppingapi.model.UserFavoriteId;
import com.example.shoppingapi.repository.UserFavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
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

    public UserFavorite updateUserFavorite(Long userId, Long productId, UserFavorite userFavorite) {
        UserFavoriteId id = new UserFavoriteId();
        id.setUserId(userId);
        id.setProductId(productId);
        Optional<UserFavorite> existingOpt = userFavoriteRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("UserFavorite not found with given composite key");
        }
        userFavorite.setId(id);
        return userFavoriteRepository.save(userFavorite);
    }

    public UserFavorite partialUpdateUserFavorite(Long userId, Long productId, Map<String, Object> updates) {
        UserFavoriteId id = new UserFavoriteId();
        id.setUserId(userId);
        id.setProductId(productId);
        Optional<UserFavorite> existingOpt = userFavoriteRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("UserFavorite not found with given composite key");
        }
        UserFavorite userFavorite = existingOpt.get();
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(UserFavorite.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, userFavorite, value);
            }
        });
        return userFavoriteRepository.save(userFavorite);
    }

    public void deleteById(Long userId, Long productId) {
        UserFavoriteId id = new UserFavoriteId();
        id.setUserId(userId);
        id.setProductId(productId);
        userFavoriteRepository.deleteById(id);
    }
}
