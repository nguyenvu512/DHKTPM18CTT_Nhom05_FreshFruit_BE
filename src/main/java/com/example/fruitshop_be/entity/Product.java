package com.example.fruitshop_be.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    double price;
    String origin;
    String description;
    int inventory;
    @ManyToOne
    Category category;
    @OneToMany(mappedBy = "product")
    List<ProductImage> images;
    @OneToMany(mappedBy = "product")
    List<CartItem> cartItems;
    @OneToMany(mappedBy = "product")
    List<OrderDetail> orderDetails;
}
