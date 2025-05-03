package com.example.shoppingapi.repository;

import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.UserCart;
import com.example.shoppingapi.model.UserCartId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCartRepository extends JpaRepository<UserCart, UserCartId> {
    @Query(value = "SELECT p.* FROM Product p INNER JOIN UserCart uc ON p.product_id = uc.product_id WHERE uc.user_id = :userId", nativeQuery = true)
    List<Product> findProductsByUserId(@Param("userId") Long userId);

    List<UserCart> findByIdProductId(Long productId);
    List<UserCart> findByIdUserId(Long userId);
}
