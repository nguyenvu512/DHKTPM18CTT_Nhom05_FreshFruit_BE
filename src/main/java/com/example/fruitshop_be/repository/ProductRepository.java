package com.example.fruitshop_be.repository;

import com.example.fruitshop_be.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
    Product findByProductId(String productId);
}
