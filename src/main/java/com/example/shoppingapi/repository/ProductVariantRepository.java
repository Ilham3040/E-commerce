 package com.example.shoppingapi.repository;

import com.example.shoppingapi.model.ProductVariant;

import java.util.Optional;

import com.example.shoppingapi.model.Store;
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
    
    @Query(value = "SELECT pd FROM ProductVariant pd WHERE pd.product.id = :id",nativeQuery = true)
    Optional<ProductVariant> findProductVariantbyProductId(@Param("id") Long id);

    @Query("SELECT p FROM ProductVariant p WHERE p.id = :id AND p.isDeleted = true")
    Optional<ProductVariant> findSoftDeletedById(Long id);
}
