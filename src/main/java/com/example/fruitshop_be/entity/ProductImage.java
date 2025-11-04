package com.example.fruitshop_be.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String url;
    @ManyToOne
    Product product;
}
