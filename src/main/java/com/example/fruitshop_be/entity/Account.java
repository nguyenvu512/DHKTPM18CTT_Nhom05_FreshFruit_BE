package com.example.fruitshop_be.entity;

import com.example.fruitshop_be.enums.Provider;
import com.example.fruitshop_be.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String username;
    String password;
    Provider provider;
    @Enumerated(EnumType.STRING)
    Role role;
    @OneToOne
    Customer customer;
}
