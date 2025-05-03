package com.example.shoppingapi.repository;

import com.example.shoppingapi.model.ProductDetail;
import com.example.shoppingapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.shoppingapi.model.Store;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findByStoreName(String storeName);

    @Query(value = "SELECT pd FROM ProductDetail pd WHERE pd.product.id = :id", nativeQuery = true)
    Optional<ProductDetail> findProductDetailbyProductId(@Param("id") Long id);
}
