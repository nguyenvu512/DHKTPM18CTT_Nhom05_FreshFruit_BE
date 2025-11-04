package com.example.fruitshop_be.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailId implements Serializable {
    String order;
    String product;
}
