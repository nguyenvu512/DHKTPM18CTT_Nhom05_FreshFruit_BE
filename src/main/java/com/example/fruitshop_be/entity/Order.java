package com.example.fruitshop_be.entity;

import com.example.fruitshop_be.enums.Payment;
import com.example.fruitshop_be.enums.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    LocalDateTime orderDate;
    double totalAmount;
    @Enumerated(EnumType.STRING)
    Payment paymentMethod;
    String shippingAddress;
    @Enumerated(EnumType.STRING)
    Status status;
    @ManyToOne
    Customer customer;
    @ManyToOne
    Voucher voucher;

}
