package com.example.fruitshop_be.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponse {
    private String productId;
    private String productName;
    private double price;
    private int quantity;
    private double total;
    private String productImage;
}