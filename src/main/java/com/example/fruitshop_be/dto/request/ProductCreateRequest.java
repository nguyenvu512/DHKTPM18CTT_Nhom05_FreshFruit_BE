package com.example.fruitshop_be.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder

public class ProductCreateRequest {
    String name;
    double price;
    String origin;
    String description;
    int inventory;
    String category;
    MultipartFile[] images;
}
