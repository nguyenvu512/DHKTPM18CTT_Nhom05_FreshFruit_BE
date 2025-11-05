package com.example.fruitshop_be.service;

import com.example.fruitshop_be.dto.response.AccountResponse;
import com.example.fruitshop_be.mapper.AccountMapper;
import com.example.fruitshop_be.repository.AccountRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
@RequiredArgsConstructor
public class AccountService {
    AccountRepository accountRepository;
    AccountMapper accountMapper;
    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream().map(accountMapper::toAccountResponse).collect(Collectors.toList());
    }
}
