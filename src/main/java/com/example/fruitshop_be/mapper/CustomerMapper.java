package com.example.fruitshop_be.mapper;

import com.example.fruitshop_be.dto.request.CustomerCreateRequest;
import com.example.fruitshop_be.dto.request.CustomerUpdateRequest;
import com.example.fruitshop_be.dto.response.CustomerResponse;
import com.example.fruitshop_be.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toCustomer(CustomerCreateRequest request);
    CustomerResponse toCustomerResponse(Customer customer);
   void updateCustomer(@MappingTarget Customer customer, CustomerUpdateRequest request);
}
