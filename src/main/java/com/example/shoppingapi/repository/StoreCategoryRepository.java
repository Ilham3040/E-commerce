package com.example.shoppingapi.repository;

import com.example.shoppingapi.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.shoppingapi.model.StoreCategory;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreCategoryRepository extends JpaRepository<StoreCategory, Long> {

    @Query("SELECT s FROM StoreCategory s WHERE s.store.storeId = :storeId")
    List<StoreCategory> findByStoreId(Long storeId);

    @Query("SELECT s FROM StoreCategory s WHERE s.id = :id AND s.isDeleted = true")
    Optional<StoreCategory> findSoftDeletedById(Long id);
}
