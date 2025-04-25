package com.example.shoppingapi.repository;

import com.example.shoppingapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.shoppingapi.model.Store;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findByStoreName(String storeName);

    @Query("SELECT s FROM Store s WHERE s.id = :id AND s.isDeleted = true")
    Optional<Store> findSoftDeletedById(Long id);
}
