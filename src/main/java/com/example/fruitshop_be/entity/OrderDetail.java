package com.example.fruitshop_be.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@IdClass(OrderDetailId.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetail {
    @Id
    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    int quantity;
}
