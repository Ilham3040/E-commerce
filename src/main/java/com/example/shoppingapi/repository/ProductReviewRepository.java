package com.example.shoppingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.shoppingapi.model.ProductReview;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    
}
