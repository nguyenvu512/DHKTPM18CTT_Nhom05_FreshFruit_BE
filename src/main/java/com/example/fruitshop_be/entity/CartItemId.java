package com.example.fruitshop_be.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Objects;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemId implements Serializable {
    String cart;
    String product;
}
