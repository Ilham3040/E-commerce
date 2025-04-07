package com.example.shoppingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shoppingapi.model.StoreCategory;

@Repository
public interface StoreCategoryRepository extends JpaRepository<StoreCategory, Long> {
}
