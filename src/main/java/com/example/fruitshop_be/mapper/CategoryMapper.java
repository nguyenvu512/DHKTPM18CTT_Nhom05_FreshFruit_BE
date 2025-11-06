package com.example.fruitshop_be.mapper;

import com.example.fruitshop_be.dto.request.CategoryCreateRequest;
import com.example.fruitshop_be.dto.request.CategoryUpdateRequest;
import com.example.fruitshop_be.dto.response.CategoryResponse;
import com.example.fruitshop_be.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryCreateRequest request);
    CategoryResponse toCategoryResponse(Category category);
    void updateCategory(@MappingTarget Category category, CategoryUpdateRequest requets);
}
