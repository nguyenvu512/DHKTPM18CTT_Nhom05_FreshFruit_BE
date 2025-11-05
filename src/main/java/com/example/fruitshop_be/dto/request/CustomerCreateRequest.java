package com.example.fruitshop_be.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CustomerCreateRequest {
    String name;
    String address;
    String phone;
    String email;
    String password;
}
