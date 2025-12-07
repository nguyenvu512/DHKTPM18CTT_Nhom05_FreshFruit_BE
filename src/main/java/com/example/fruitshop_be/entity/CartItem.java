package com.example.fruitshop_be.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@IdClass(CartItemId.class)
public class CartItem {

    @Id
    @ManyToOne
    @JoinColumn(name = "cart_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Cart cart;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Product product;

    int quantity;
}
