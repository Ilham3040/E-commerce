package com.example.shoppingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.shoppingapi.model.Product;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStoreStoreId(Long storeId);

    Product findByProductName(String productName);
}
