package com.example.fruitshop_be.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ProductImageResponse {
    String id;
    String url;
    String publicId;
}
