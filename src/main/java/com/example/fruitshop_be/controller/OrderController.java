package com.example.fruitshop_be.controller;

import com.example.fruitshop_be.dto.ApiResponse;
import com.example.fruitshop_be.dto.request.CustomerCreateRequest;
import com.example.fruitshop_be.dto.request.OrderCreateRequest;
import com.example.fruitshop_be.dto.request.UpdateOrderStatusRequest;
import com.example.fruitshop_be.dto.response.CustomerResponse;
import com.example.fruitshop_be.dto.response.OrderResponse;
import com.example.fruitshop_be.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/order")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderController {

    OrderService orderService;

    @GetMapping
    public ApiResponse<List<OrderResponse>> getAllOrders() {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getAllOrders())
                .build();
    }

    @GetMapping("/{customerId}")
    public ApiResponse<List<OrderResponse>> getOrdersByCustomer(
            @PathVariable String customerId
    ) {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getOrdersByCustomer(customerId))
                .build();
    }

    @PostMapping
    public ApiResponse<OrderResponse> createOrder(
            @RequestBody OrderCreateRequest request,
            HttpServletRequest httpRequest  // Thêm dòng này
    ) throws UnsupportedEncodingException {

        OrderResponse response = orderService.createOrder(request, httpRequest);

        return ApiResponse.<OrderResponse>builder()
                .result(response)
                .build();
    }

    @PutMapping("/update-status")
    public ApiResponse<OrderResponse> updateOrderStatus(@RequestBody UpdateOrderStatusRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.updateOrderStatus(request))
                .build();
    }
}
