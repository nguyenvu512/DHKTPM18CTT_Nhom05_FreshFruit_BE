package com.example.fruitshop_be.controller;

import com.example.fruitshop_be.dto.ApiResponse;
import com.example.fruitshop_be.dto.request.CategoryCreateRequest;
import com.example.fruitshop_be.dto.request.CategoryUpdateRequest;
import com.example.fruitshop_be.dto.response.CategoryResponse;
import com.example.fruitshop_be.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class CategoryController {
   CategoryService categoryService;
   @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
       return  ApiResponse.<List<CategoryResponse>>builder()
               .result(categoryService.getAllCategories())
               .build();
   }
   @PostMapping
   public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryCreateRequest request) {
       return ApiResponse.<CategoryResponse>builder()
               .result(categoryService.createCategory(request))
               .build();
   }
   @PutMapping("{id}")
    public ApiResponse<CategoryResponse> updateCategori(@PathVariable String id, @RequestBody CategoryUpdateRequest request) {
       return ApiResponse.<CategoryResponse>builder()
               .result(categoryService.updateCategory(id,request))
               .build();
   }
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ApiResponse.<String>builder()
                .result("Xóa danh mục thành công")
                .build();
    }





}
