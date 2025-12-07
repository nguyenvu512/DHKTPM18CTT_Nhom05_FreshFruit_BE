package com.example.fruitshop_be.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemUpdateRequest {
    String customerId;
    String productId;
    int quantity;
}
