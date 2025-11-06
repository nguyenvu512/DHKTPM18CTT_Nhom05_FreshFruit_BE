package com.example.fruitshop_be.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemRequest {
    String productID;
    int quantity;
}
