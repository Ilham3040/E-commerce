package com.example.shoppingapi.repository;

import com.example.shoppingapi.model.UserCart;
import com.example.shoppingapi.model.UserCartId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCartRepository extends JpaRepository<UserCart, UserCartId> {
}
