package com.example.fruitshop_be.cloudinary;

import com.example.fruitshop_be.entity.Product;
import com.example.fruitshop_be.entity.ProductImage;
import com.example.fruitshop_be.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class CloudinaryController {

    private final CloudinaryService cloudinaryService;
    private final ProductRepository productRepository;

    @PostMapping("/{productId}")
    public List<ProductImage> uploadImages(
            @PathVariable String productId,
            @RequestParam("files") MultipartFile[] files) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found!"));

        return cloudinaryService.uploadMultipleFiles(files, product);
    }

    @DeleteMapping("/{publicId}")
    public void deleteImage(@PathVariable String publicId) {
        cloudinaryService.deleteFile(publicId);
    }
}
