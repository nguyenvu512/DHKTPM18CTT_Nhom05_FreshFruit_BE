package com.example.fruitshop_be.repository;

import com.example.fruitshop_be.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository  extends JpaRepository<Customer, String> {
    Customer findByEmail(String email);
}
