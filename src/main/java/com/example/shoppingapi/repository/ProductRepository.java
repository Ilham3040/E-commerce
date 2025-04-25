package com.example.shoppingapi.repository;

import com.example.shoppingapi.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.shoppingapi.model.Product;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStoreStoreId(Long storeId);

    Product findByProductName(String productName);
    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.isDeleted = true")
    Optional<Product> findSoftDeletedById(Long id);
}
