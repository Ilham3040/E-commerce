package com.example.shoppingapi.repository;

import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.StoreDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreDetailRepository extends JpaRepository<StoreDetail, Long> {
    @Query("SELECT s FROM StoreDetail s WHERE s.id = :id AND s.isDeleted = true")
    Optional<StoreDetail> findSoftDeletedById(Long id);
}
