package com.example.fruitshop_be.repository;

import com.example.fruitshop_be.entity.CartItem;
import com.example.fruitshop_be.entity.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
}
