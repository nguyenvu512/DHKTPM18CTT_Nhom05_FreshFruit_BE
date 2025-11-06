package com.example.fruitshop_be.dto.response;


import com.example.fruitshop_be.entity.ProductImage;
import com.example.fruitshop_be.repository.ProductImageRepository;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String id;
    String name;
    double price;
    String origin;
    String description;
    int inventory;
    String category;
    List<ProductImageResponse> images;
}
