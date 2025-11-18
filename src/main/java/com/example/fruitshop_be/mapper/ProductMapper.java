package com.example.fruitshop_be.mapper;

import com.example.fruitshop_be.dto.request.ProductCreateRequest;
import com.example.fruitshop_be.dto.request.ProductUpdateRequest;
import com.example.fruitshop_be.dto.response.ProductResponse;
import com.example.fruitshop_be.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface ProductMapper {
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "images", ignore = true)
    Product toProduct(ProductCreateRequest request);

    @Mapping(target = "category",source = "category.id")
    ProductResponse toProductResponse(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "images", ignore = true) // xử lý riêng
    @Mapping(target = "category", ignore = true) // set riêng
    void updateProduct(ProductUpdateRequest request, @MappingTarget Product product);

}
