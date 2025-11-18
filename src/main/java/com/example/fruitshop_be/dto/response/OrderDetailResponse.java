package com.example.fruitshop_be.dto.response;

import com.example.fruitshop_be.enums.Payment;
import com.example.fruitshop_be.enums.Status;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderDetailResponse {
    private String productId;
    private int quantity;
}
