package com.example.fruitshop_be.mapper;

import com.example.fruitshop_be.dto.response.AccountResponse;
import com.example.fruitshop_be.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountResponse toAccountResponse(Account account);
}
