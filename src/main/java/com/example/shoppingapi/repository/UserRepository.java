package com.example.shoppingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.shoppingapi.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}

