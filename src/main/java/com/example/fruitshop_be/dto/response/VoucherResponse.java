package com.example.fruitshop_be.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherResponse {
    String name;
    String description;
    double discount;
    int quantity;
}
