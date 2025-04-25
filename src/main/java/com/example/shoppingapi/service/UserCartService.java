package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.UserCart;
import com.example.shoppingapi.model.UserCartId;
import com.example.shoppingapi.repository.UserCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCartService {

    private final UserCartRepository userCartRepository;

    public List<Product> getAllByUserId(Long id) {
        return userCartRepository.findProductsByUserId(id);
    }

    public UserCart findById(UserCartId id) {
        return userCartRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("UserCart not found with ID: " + id));
    }

    public UserCart addingUserCart(UserCart userCart) {
        return userCartRepository.save(userCart);
    }


    public void deleteById(UserCartId id) {
        findById(id);
        userCartRepository.deleteById(id);
    }
}
