package com.example.fruitshop_be.dto.response;

import com.example.fruitshop_be.enums.Payment;
import com.example.fruitshop_be.enums.Status;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderResponse {
    private String id;

    private LocalDateTime orderDate;

    private double totalAmount;

    private Payment paymentMethod;

    private String shippingAddress;

    private Status status;

    private String customerId;

    private String voucherId;

    private List<OrderDetailResponse> items;

    private String fullName;

    private String phoneNumber;

    String paymentUrl;

    String paymentStatus;

    String errorNote;

    LocalDateTime shippingDate;
}
