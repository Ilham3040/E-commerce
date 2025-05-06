package com.example.shoppingapi.repository;

import java.util.Optional;

import com.example.shoppingapi.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.shoppingapi.model.ProductDetail;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {

    @Query("SELECT pd FROM ProductDetail pd WHERE pd.product.id = :id")
    Optional<ProductDetail> findProductDetailbyProductId(@Param("id") Long id);

    @Query("SELECT p FROM ProductDetail p WHERE p.id = :id AND p.isDeleted = true")
    Optional<ProductDetail> findSoftDeletedById(Long id);
}
