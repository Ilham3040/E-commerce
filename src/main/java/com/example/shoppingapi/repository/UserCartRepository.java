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
    @Query("SELECT p FROM Product p JOIN UserCart uc ON p.productId = uc.productId WHERE uc.userId = :userId")
    List<Product> findProductsByUserId(@Param("userId") Long userId);
    List<UserCart> findByIdProductId(Long productId);
    List<UserCart> findByIdUserId(Long userId);
}
