 package com.example.shoppingapi.repository;

import com.example.shoppingapi.model.ProductVariant;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for ProductVariant entity.
 * Provides CRUD operations and custom query methods through JpaRepository.
 */
@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    
    @Query("SELECT pd FROM ProductVariant pd WHERE pd.product.id = :id")
    Optional<ProductVariant> findProductVariantbyProductId(@Param("id") Long id);
}
