package com.example.fruitshop_be.service;

import com.example.fruitshop_be.cloudinary.CloudinaryService;
import com.example.fruitshop_be.dto.request.ProductCreateRequest;
import com.example.fruitshop_be.dto.request.ProductUpdateRequest;
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
import java.util.stream.Collectors;

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
    public ProductResponse updateProduct(String id, ProductUpdateRequest request){
        Product product = productRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND));
        productMapper.updateProduct(request,product);
        if(request.getCategory()!=null){
            Category category = categoryRepository.findById(request.getCategory()).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND));
            product.setCategory(category);
        }
        if(request.getImages()!=null && request.getImages().length>0){
            List<ProductImage> OldImg = product.getImages();

            if(product.getImages()!= null && !product.getImages().isEmpty()){
                for(ProductImage image: OldImg){
                    cloudinaryService.deleteFile(image.getPublicId());
                }
                product.getImages().clear();
            }
            List<ProductImage> newImages = cloudinaryService.uploadMultipleFiles(request.getImages(),product);
            product.setImages(newImages);
        }
        productRepository.save(product);
        return productMapper.toProductResponse(product);
    }
    public List<ProductResponse> searchProductByName(String name){
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name.trim());
//        System.out.println(name);
        return products.stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }
    public ProductResponse getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        return productMapper.toProductResponse(product);
    }




}
