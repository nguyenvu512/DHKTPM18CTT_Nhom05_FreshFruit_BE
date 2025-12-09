package com.example.fruitshop_be.repository;

import com.example.fruitshop_be.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {
    Category findByName(String name);
    List<Category> findByNameContainingIgnoreCase(String name);

}
