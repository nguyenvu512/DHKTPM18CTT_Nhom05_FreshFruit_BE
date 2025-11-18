package com.example.fruitshop_be.controller;

import com.example.fruitshop_be.dto.ApiResponse;
import com.example.fruitshop_be.dto.request.CustomerCreateRequest;
import com.example.fruitshop_be.dto.request.OrderCreateRequest;
import com.example.fruitshop_be.dto.response.CustomerResponse;
import com.example.fruitshop_be.dto.response.OrderResponse;
import com.example.fruitshop_be.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderController {

    OrderService orderService;

    @PostMapping
    public ApiResponse<OrderResponse> createCustomer(@RequestBody OrderCreateRequest request){
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.createOrder(request))
                .build();
    }
}
