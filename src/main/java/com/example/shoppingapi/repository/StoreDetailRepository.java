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

    @Query("SELECT sd FROM StoreDetail sd WHERE sd.store.id = :storeId")
    Optional<StoreDetail> findStoreDetailbyStoreId(@Param("storeId") Long storeId);

    @Query("SELECT sd FROM StoreDetail sd WHERE sd.store.id = :storeId")
    Optional<StoreDetail> findStoreDetailByStoreId(@Param("storeId") Long storeId);

}

