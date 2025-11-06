package com.example.fruitshop_be.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @OneToOne
    Customer customer;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<CartItem> cartItems;
}
