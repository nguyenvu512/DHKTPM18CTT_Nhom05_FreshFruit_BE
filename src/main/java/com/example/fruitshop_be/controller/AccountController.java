package com.example.fruitshop_be.controller;

import com.example.fruitshop_be.dto.ApiResponse;
import com.example.fruitshop_be.dto.response.AccountResponse;
import com.example.fruitshop_be.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class AccountController {
    AccountService accountService;


    @GetMapping
    public ApiResponse<List<AccountResponse>> getAllAccounts() {
        return ApiResponse.<List<AccountResponse>>builder()
                .result(accountService.getAllAccounts())
                .build();
    }
}
