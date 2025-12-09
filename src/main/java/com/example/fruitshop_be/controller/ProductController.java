package com.example.fruitshop_be.controller;

import com.example.fruitshop_be.dto.ApiResponse;
import com.example.fruitshop_be.dto.request.ProductCreateRequest;
import com.example.fruitshop_be.dto.request.ProductUpdateRequest;
import com.example.fruitshop_be.dto.response.ProductResponse;
import com.example.fruitshop_be.entity.Product;
import com.example.fruitshop_be.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor

public class ProductController {
    ProductService productService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductResponse> createProduct(@ModelAttribute ProductCreateRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(request))
                .build();

    }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ApiResponse.<Void>builder()
                .message("Product deleted")
                .build();
    }
    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable String id, @ModelAttribute ProductUpdateRequest request){
        return ApiResponse.<ProductResponse>builder()
                .result(productService.updateProduct(id,request))
                .build();
    }
    @GetMapping("/search")
    public ApiResponse<List<ProductResponse>> searchProduct(@RequestParam String name){
        List<ProductResponse> products = productService.searchProductByName(name.trim());
        System.out.println(name);
        return ApiResponse.<List<ProductResponse>>builder()
                .result(products)
                .build();
    }


    @GetMapping
    public ApiResponse<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ApiResponse.<List<ProductResponse>>builder()
                .result(products)
                .build();
    }
    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable String id) {
        ProductResponse product = productService.getProductById(id);
        return ApiResponse.<ProductResponse>builder()
                .result(product)
                .build();
    }


}
