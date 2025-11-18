package com.example.fruitshop_be.dto.request;

import com.example.fruitshop_be.enums.Payment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderCreateRequest {
    private String customerId;
    private String voucherId;
    private Payment paymentMethod;
    private String shippingAddress;
    private Double totalAmount;
    private List<OrderDetailRequest> items;
}
