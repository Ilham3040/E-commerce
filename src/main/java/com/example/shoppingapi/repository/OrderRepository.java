package com.example.shoppingapi.repository;

import com.example.shoppingapi.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.user.userId = :userId")
    List<Order> findByUserUserId(Long userId);

    @Query("SELECT o FROM Order o WHERE o.product.productId = :productId")
    List<Order> findByProductProductId(Long productId);
}
