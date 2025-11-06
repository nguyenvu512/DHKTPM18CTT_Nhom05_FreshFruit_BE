package com.example.fruitshop_be.mapper;

import com.example.fruitshop_be.dto.request.CartItemRequest;
import com.example.fruitshop_be.dto.response.CartItemResponse;
import com.example.fruitshop_be.entity.CartItem;
import com.example.fruitshop_be.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItem toCartItem(CartItemRequest request);

    @Mapping(target = "product_id", source = "product.id")
    @Mapping(target = "product_name", source = "product.name")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "product_image", source = "product", qualifiedByName = "getFirstImageUrl")
    CartItemResponse toCartItemResponse(CartItem cartItem);

    void updateCartItem(@MappingTarget CartItem cartItem, CartItemRequest request);

    // ✅ Phải nhận Product, không phải List<Image>
    @Named("getFirstImageUrl")
    default String getFirstImageUrl(Product product) {
        if (product == null || product.getImages() == null || product.getImages().isEmpty()) {
            return null;
        }
        return product.getImages().get(0).getUrl();
    }
}
