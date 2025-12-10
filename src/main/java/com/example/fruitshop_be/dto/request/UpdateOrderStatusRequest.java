package com.example.fruitshop_be.dto.request;

import com.example.fruitshop_be.enums.Status;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateOrderStatusRequest {
    private String orderId;
    private String username;
    private Status status;
    private String description;
}
