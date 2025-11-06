package com.example.fruitshop_be.service;

import com.example.fruitshop_be.dto.request.CategoryCreateRequest;
import com.example.fruitshop_be.dto.request.CategoryUpdateRequest;
import com.example.fruitshop_be.dto.response.CategoryResponse;
import com.example.fruitshop_be.entity.Category;
import com.example.fruitshop_be.enums.ErrorCode;
import com.example.fruitshop_be.exception.AppException;
import com.example.fruitshop_be.mapper.CategoryMapper;
import com.example.fruitshop_be.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
@RequiredArgsConstructor
public class CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(categoryMapper::toCategoryResponse).collect(Collectors.toList());
    }
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        Category category = categoryRepository.save(categoryMapper.toCategory(request));
        return categoryMapper.toCategoryResponse(category);
    }
    public CategoryResponse updateCategory(String id, CategoryUpdateRequest request){
        Category category = categoryRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND));
        categoryMapper.updateCategory(category,request);
        categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);

    }
    public void deleteCategory(String id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục có id: " + id));
        categoryRepository.delete(category);
    }

}
