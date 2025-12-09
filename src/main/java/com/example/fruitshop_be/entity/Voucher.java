package com.example.fruitshop_be.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Getter
@Setter
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    String description;
    double discount;
    int quantity;
}
