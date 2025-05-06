package com.example.shoppingapi.repository;

import com.example.shoppingapi.model.OrderItem;
import com.example.shoppingapi.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Fetch all order items by order ID
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.orderId = :orderId")
    List<OrderItem> findByOrderOrderId(Long orderId);

    // Fetch all order items by product ID
    @Query("SELECT oi FROM OrderItem oi WHERE oi.product.productId = :productId")
    List<OrderItem> findByProductProductId(Long productId);

    // Fetch all order items by product variant ID
    @Query("SELECT oi FROM OrderItem oi WHERE oi.productVariant.variantId = :productVariantId")
    List<OrderItem> findByProductVariantProductVariantsId(Long productVariantId);
}
