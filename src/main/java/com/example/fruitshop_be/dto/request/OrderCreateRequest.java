package com.example.fruitshop_be.dto.request;

import com.example.fruitshop_be.enums.Payment;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderCreateRequest {
    private LocalDateTime orderDate;
    private String customerId;
    private String voucherId;

    @Enumerated(EnumType.STRING)
    private Payment paymentMethod;

    private String shippingAddress;
    private Double totalAmount;
    private List<OrderDetailRequest> items;
    private String fullName;
    private String phoneNumber;
}
