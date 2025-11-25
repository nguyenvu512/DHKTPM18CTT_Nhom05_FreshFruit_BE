package com.example.fruitshop_be.mapper;

import com.example.fruitshop_be.dto.response.CartResponse;
import com.example.fruitshop_be.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CartItemMapper.class)
public interface CartMapper {

    @Mapping(target = "cartId", source = "id") // map cart.id → cartId
    @Mapping(target = "customerId", expression = "java(cart.getCustomer() != null ? cart.getCustomer().getId() : null)")
    @Mapping(target = "items", source = "cartItems")
    @Mapping(target = "totalPrice", expression = "java(cart.getCartItems() != null ? cart.getCartItems().stream().mapToDouble(i -> i.getQuantity() * i.getProduct().getPrice()).sum() : 0)")
    CartResponse toCartResponse(Cart cart);
}
