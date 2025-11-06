package com.example.fruitshop_be.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartUpdateRequest {
    String cartId;
    String productId;
    Integer quantity;
}
