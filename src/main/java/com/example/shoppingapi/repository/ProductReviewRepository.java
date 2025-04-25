package com.example.shoppingapi.repository;

import com.example.shoppingapi.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.shoppingapi.model.ProductReview;

import java.util.Optional;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    @Query("SELECT p FROM ProductReview p WHERE p.id = :id AND p.isDeleted = true")
    Optional<ProductReview> findSoftDeletedById(Long id);
}
