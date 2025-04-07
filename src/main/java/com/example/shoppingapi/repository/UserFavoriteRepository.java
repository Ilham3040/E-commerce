package com.example.shoppingapi.repository;

import com.example.shoppingapi.model.UserFavorite;
import com.example.shoppingapi.model.UserFavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFavoriteRepository extends JpaRepository<UserFavorite, UserFavoriteId> {
}
