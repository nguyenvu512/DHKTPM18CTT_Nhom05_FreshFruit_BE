package com.example.fruitshop_be.service;

import com.example.fruitshop_be.dto.request.CustomerCreateRequest;
import com.example.fruitshop_be.dto.request.CustomerUpdateRequest;
import com.example.fruitshop_be.dto.response.CustomerResponse;
import com.example.fruitshop_be.entity.Account;
import com.example.fruitshop_be.entity.Customer;
import com.example.fruitshop_be.enums.ErrorCode;
import com.example.fruitshop_be.enums.Provider;
import com.example.fruitshop_be.enums.Role;
import com.example.fruitshop_be.exception.AppException;
import com.example.fruitshop_be.mapper.CustomerMapper;
import com.example.fruitshop_be.repository.AccountRepository;
import com.example.fruitshop_be.repository.CustomerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
@RequiredArgsConstructor
public class CustomerService {
    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;
    public CustomerResponse createCustomer(CustomerCreateRequest request) {
            Customer existCustomer = customerRepository.findByEmail(request.getEmail());
            if (existCustomer!=null) {
                throw  new AppException(ErrorCode.CUSTOMER_EXISTS);
            }
        Customer customer = customerRepository.save(customerMapper.toCustomer(request));
        Account account = new Account();
        account.setCustomer(customer);
        account.setRole(Role.CUSTOMER);
        account.setUsername(customer.getEmail());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setProvider(Provider.LOCAL);
        accountRepository.save(account);
        return customerMapper.toCustomerResponse(customer);
    }
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::toCustomerResponse).collect(Collectors.toList());
    }
    public CustomerResponse updateCustomer(String id, CustomerUpdateRequest request) {
        Customer customer = customerRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND));
        customerMapper.updateCustomer(customer, request);
        return customerMapper.toCustomerResponse(customerRepository.save(customer));
    }
}
