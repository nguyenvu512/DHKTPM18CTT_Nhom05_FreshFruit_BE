package com.example.fruitshop_be.mapper;

import com.example.fruitshop_be.dto.request.OrderCreateRequest;
import com.example.fruitshop_be.dto.response.OrderResponse;
import com.example.fruitshop_be.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderMapper.class})
public interface OrderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", expression = "java(Status.PENDING)")
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "voucher", ignore = true)
    Order toOrder(OrderCreateRequest request);

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "voucherId", source = "voucher.id")
    OrderResponse toOrderResponse(Order order);
}
