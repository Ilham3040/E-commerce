package com.example.shoppingapi.repository;

import com.example.shoppingapi.model.StoreDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreDetailRepository extends JpaRepository<StoreDetail, Long> {
}
