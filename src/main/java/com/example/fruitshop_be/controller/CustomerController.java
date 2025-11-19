package com.example.fruitshop_be.controller;

import com.example.fruitshop_be.dto.ApiResponse;
import com.example.fruitshop_be.dto.request.CustomerCreateRequest;
import com.example.fruitshop_be.dto.request.CustomerUpdateRequest;
import com.example.fruitshop_be.dto.response.CustomerResponse;
import com.example.fruitshop_be.service.CustomerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class CustomerController {
    CustomerService customerService;
    StringRedisTemplate template;

    @PostMapping
    public ApiResponse<CustomerResponse> createCustomer(@RequestBody CustomerCreateRequest request){
        return ApiResponse.<CustomerResponse>builder()
                .result(customerService.createCustomer(request))
                .build();
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<CustomerResponse>> getAllCustomers(){
        return ApiResponse.<List<CustomerResponse>>builder()
                .result(customerService.getAllCustomers())
                .build();
    }
    @PutMapping("/{id}")
    public ApiResponse<CustomerResponse> updateCustomer(@PathVariable String id, @RequestBody CustomerUpdateRequest request){
        return ApiResponse.<CustomerResponse>builder()
                .result(customerService.updateCustomer(id, request))
                .build();
    }
}
