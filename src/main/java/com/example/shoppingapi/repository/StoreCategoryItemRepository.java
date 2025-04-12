package com.example.shoppingapi.repository;

import com.example.shoppingapi.model.StoreCategoryItem;
import com.example.shoppingapi.model.StoreCategoryItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreCategoryItemRepository extends JpaRepository<StoreCategoryItem, StoreCategoryItemId> {
}
