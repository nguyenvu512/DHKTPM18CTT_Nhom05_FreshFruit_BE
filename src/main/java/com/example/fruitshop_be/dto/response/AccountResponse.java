package com.example.fruitshop_be.dto.response;

import com.example.fruitshop_be.enums.Provider;
import com.example.fruitshop_be.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {
    String id;
    String username;
    @Enumerated(EnumType.STRING)
    Provider provider;
    @Enumerated(EnumType.STRING)
    Role role;
}
