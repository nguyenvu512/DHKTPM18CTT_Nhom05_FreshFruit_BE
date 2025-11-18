package com.example.fruitshop_be.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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
    String publicId;
    @ManyToOne
    Product product;
}
