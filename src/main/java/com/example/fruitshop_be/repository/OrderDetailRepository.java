package com.example.fruitshop_be.repository;

import com.example.fruitshop_be.entity.Order;
import com.example.fruitshop_be.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,String> {
    List<OrderDetail> findByOrder(Order order);
}
