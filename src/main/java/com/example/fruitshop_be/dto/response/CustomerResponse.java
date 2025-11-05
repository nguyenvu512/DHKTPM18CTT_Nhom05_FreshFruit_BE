package com.example.fruitshop_be.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerResponse {
    String id;
    String name;
    String address;
    String phone;
    String email;
}
