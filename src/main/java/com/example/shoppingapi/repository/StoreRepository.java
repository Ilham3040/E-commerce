package com.example.shoppingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.shoppingapi.model.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    // Custom query method to find a store by name
    Store findByStoreName(String storeName);
}
