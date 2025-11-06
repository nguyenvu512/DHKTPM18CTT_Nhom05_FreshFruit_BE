package com.example.fruitshop_be.controller;

import com.example.fruitshop_be.dto.ApiResponse;
import com.example.fruitshop_be.dto.request.VoucherCreateRequest;
import com.example.fruitshop_be.dto.request.VoucherUpdateRequest;
import com.example.fruitshop_be.dto.response.VoucherResponse;
import com.example.fruitshop_be.service.VoucherService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voucher")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor

public class VoucherController {
    VoucherService voucherService;
    StringRedisTemplate template;

    @PostMapping
    public ApiResponse<VoucherResponse> createVoucher(@RequestBody VoucherCreateRequest request) {
        return ApiResponse.<VoucherResponse>builder().result(voucherService.createVoucher(request)).build();

    }

    @GetMapping
    public ApiResponse<List<VoucherResponse>> getAllVouchers() {
        return  ApiResponse.<List<VoucherResponse>>builder().result(voucherService.getAllVouchers()).build();

    }

    @PutMapping("{id}")
    public ApiResponse<VoucherResponse> updateVoucher(@PathVariable("id") String id, @RequestBody VoucherUpdateRequest request) {
        return ApiResponse.<VoucherResponse>builder().result(voucherService.updateVoucher(id, request)).build();
    }

    @GetMapping("/redis-test")
    public String test() {
        template.opsForValue().set("key", "Hello Redis!!!");
        return template.opsForValue().get("key");
    }


}
