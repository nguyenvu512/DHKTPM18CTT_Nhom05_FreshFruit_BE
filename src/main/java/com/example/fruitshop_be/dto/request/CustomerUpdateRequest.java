package com.example.fruitshop_be.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerUpdateRequest {
    String name;
    String address;
    String phone;
}
