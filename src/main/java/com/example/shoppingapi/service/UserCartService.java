package com.example.shoppingapi.service;

import com.example.shoppingapi.model.UserCart;
import com.example.shoppingapi.model.UserCartId;
import com.example.shoppingapi.repository.UserCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserCartService {

    private final UserCartRepository userCartRepository;

    public List<UserCart> findAll() {
        return userCartRepository.findAll();
    }

    public UserCart findById(UserCartId id) {
        return userCartRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("UserCart not found with ID: " + id));
    }

    public UserCart saveUserCart(UserCart userCart) {
        return userCartRepository.save(userCart);
    }

    public UserCart updateUserCart(UserCartId id, UserCart userCart) {
        findById(id);
        userCart.setId(id);
        return userCartRepository.save(userCart);
    }

    public UserCart partialUpdateUserCart(UserCartId id, Map<String, Object> updates) {
        UserCart existing = findById(id);
        BeanWrapper wrapper = new BeanWrapperImpl(existing);
        updates.forEach(wrapper::setPropertyValue);
        return userCartRepository.save(existing);
    }

    public void deleteById(UserCartId id) {
        findById(id);
        userCartRepository.deleteById(id);
    }
}
