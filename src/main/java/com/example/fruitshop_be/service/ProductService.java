package com.example.fruitshop_be.service;

import com.example.fruitshop_be.cloudinary.CloudinaryService;
import com.example.fruitshop_be.dto.request.ProductCreateRequest;
import com.example.fruitshop_be.dto.response.ProductResponse;
import com.example.fruitshop_be.entity.Category;
import com.example.fruitshop_be.entity.Product;
import com.example.fruitshop_be.entity.ProductImage;
import com.example.fruitshop_be.enums.ErrorCode;
import com.example.fruitshop_be.exception.AppException;
import com.example.fruitshop_be.mapper.ProductMapper;
import com.example.fruitshop_be.repository.CategoryRepository;
import com.example.fruitshop_be.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
@RequiredArgsConstructor
public class ProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    CloudinaryService cloudinaryService;
    ProductMapper productMapper;
//    public void createProduct(ProductCreateRequest request) {
//        Product product = productMapper.toProduct(request);
//        Category category= categoryRepository.findById(request.getCategory()).get();
//        product.setCategory(category);
//        productRepository.save(product);
//        List<ProductImage> images = cloudinaryService.uploadMultipleFiles(request.getImages(), product);
//        product.setImages(images);
//        productRepository.save(product);
//    }

    public ProductResponse createProduct(ProductCreateRequest request) {

        Category category = categoryRepository.findById(request.getCategory())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        Product product = productRepository.save(productMapper.toProduct(request));
        product.setCategory(category);
        List<ProductImage> images = cloudinaryService.uploadMultipleFiles(request.getImages(), product);
        product.setImages(images);
        productRepository.save(product);
        return productMapper.toProductResponse(product);
    }
    public void deleteProduct(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        product.getImages().forEach(image->{
            cloudinaryService.deleteFile(image.getPublicId());
        });
        productRepository.delete(product);

    }



}
