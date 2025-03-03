package com.example.shoppingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.shoppingapi.model.Order;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Find all orders by user ID
    List<Order> findByUserUserId(Long userId);

    // Find all orders by store ID
    List<Order> findByProductProductId(Long productId);
}
