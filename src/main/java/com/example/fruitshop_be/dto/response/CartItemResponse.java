package com.example.fruitshop_be.dto.response;

import com.example.fruitshop_be.entity.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    String product_id;
    double price;
    String product_name;
    String product_image;
    int quantity;
}
