package com.example.fruitshop_be.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponse {
    String id;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    List<CartItemResponse> cartItems;
}
