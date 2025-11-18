package com.example.fruitshop_be.mapper;

import com.example.fruitshop_be.dto.request.OrderDetailRequest;
import com.example.fruitshop_be.dto.response.OrderDetailResponse;
import com.example.fruitshop_be.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderDetailMapper.class})
public interface OrderDetailMapper {

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    OrderDetail toOrderDetail(OrderDetailRequest request);

    @Mapping(target = "productId", source = "product.id")
    OrderDetailResponse toOrderDetailResponse(OrderDetail detail);
}
