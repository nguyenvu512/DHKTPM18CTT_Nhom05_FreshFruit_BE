package com.example.fruitshop_be.mapper;

import com.example.fruitshop_be.dto.response.CartItemResponse;
import com.example.fruitshop_be.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "total", expression = "java(item.getQuantity() * item.getProduct().getPrice())")
    @Mapping(target = "productImage", expression = "java(item.getProduct().getImages() != null && !item.getProduct().getImages().isEmpty() ? item.getProduct().getImages().get(0).getUrl() : null)")
    CartItemResponse toCartItemResponse(CartItem item);
}
