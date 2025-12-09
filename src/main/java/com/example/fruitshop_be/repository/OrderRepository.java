package com.example.fruitshop_be.repository;

import com.example.fruitshop_be.entity.Customer;
import com.example.fruitshop_be.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,String> {
    List<Order> findByCustomer(Customer customer);
}
