package com.example.shoppingapi.repository;

import com.example.shoppingapi.model.StoreRole;
import com.example.shoppingapi.model.StoreRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRoleRepository extends JpaRepository<StoreRole, StoreRoleId> {
}
