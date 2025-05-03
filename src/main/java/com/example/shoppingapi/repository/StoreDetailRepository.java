package com.example.shoppingapi.repository;

import com.example.shoppingapi.model.ProductDetail;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.StoreDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreDetailRepository extends JpaRepository<StoreDetail, Long> {

    @Query(value = "SELECT sd FROM StoreDetail sd WHERE sd.product.id = :id",nativeQuery = true)
    Optional<StoreDetail> findStoreDetailbyProductId(@Param("id") Long id);

    @Query(value = "SELECT s FROM StoreDetail s WHERE s.id = :id AND s.isDeleted = true",nativeQuery = true)
    Optional<StoreDetail> findSoftDeletedById(Long id);
}

