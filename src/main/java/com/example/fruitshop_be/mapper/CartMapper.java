package com.example.fruitshop_be.mapper;

import com.example.fruitshop_be.dto.request.CartCreateRequest;
import com.example.fruitshop_be.dto.request.CartUpdateRequest;
import com.example.fruitshop_be.dto.response.CartResponse;
import com.example.fruitshop_be.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class})
public interface CartMapper {
    Cart toCart(CartCreateRequest request);
    CartResponse toCartResponse(Cart cart);
    void updateCart(@MappingTarget Cart cart, CartUpdateRequest request);
}
