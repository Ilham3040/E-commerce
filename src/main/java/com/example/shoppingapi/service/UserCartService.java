package com.example.shoppingapi.service;

import com.example.shoppingapi.model.UserCart;
import com.example.shoppingapi.model.UserCartId;
import com.example.shoppingapi.repository.UserCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserCartService {

    @Autowired
    private UserCartRepository userCartRepository;

    public List<UserCart> findAll() {
        return userCartRepository.findAll();
    }

    public Optional<UserCart> findById(UserCartId id) {
        return userCartRepository.findById(id);
    }

    public UserCart saveUserCart(UserCart userCart) {
        return userCartRepository.save(userCart);
    }

    public UserCart updateUserCart(UserCartId id, UserCart userCart) {
        Optional<UserCart> existingOpt = userCartRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("UserCart not found with ID: " + id);
        }
        userCart.setId(id);
        return userCartRepository.save(userCart);
    }

    public UserCart partialUpdateUserCart(UserCartId id, Map<String, Object> updates) {
        Optional<UserCart> existingOpt = userCartRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("UserCart not found with ID: " + id);
        }
        UserCart userCart = existingOpt.get();
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(UserCart.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, userCart, value);
            }
        });
        return userCartRepository.save(userCart);
    }

    public void deleteUserCart(UserCartId id) {
        if (!userCartRepository.existsById(id)) {
            throw new ResourceNotFoundException("UserCart not found with ID: " + id);
        }
        userCartRepository.deleteById(id);
    }
}
