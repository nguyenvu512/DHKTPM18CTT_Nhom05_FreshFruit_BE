package com.example.fruitshop_be.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.fruitshop_be.entity.Product;
import com.example.fruitshop_be.entity.ProductImage;
import com.example.fruitshop_be.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;
    private final ProductImageRepository imageRepository;

    /**
     * 🟢 Upload 1 ảnh và lưu vào DB
     */
    public ProductImage uploadFile(MultipartFile file, Product product) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", "fruitshop_images"));
            ProductImage image = new ProductImage();
            image.setUrl(uploadResult.get("url").toString());
            image.setPublicId(uploadResult.get("public_id").toString());
            image.setProduct(product);
            return imageRepository.save(image);
        } catch (IOException e) {
            throw new RuntimeException("Upload file failed: " + e.getMessage());
        }
    }

    /**
     * 🟡 Upload nhiều ảnh cùng lúc và lưu DB
     */
    public List<ProductImage> uploadMultipleFiles(MultipartFile[] files, Product product) {
        List<ProductImage> savedImages = new ArrayList<>();
        for (MultipartFile file : files) {
            savedImages.add(uploadFile(file, product));
        }
        return savedImages;
    }

    /**
     * 🔴 Xóa ảnh cả Cloudinary và DB
     */
    public void deleteFile(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            imageRepository.findAll()
                    .stream()
                    .filter(img -> img.getPublicId().equals(publicId))
                    .findFirst()
                    .ifPresent(imageRepository::delete);
        } catch (IOException e) {
            throw new RuntimeException("Delete file failed: " + e.getMessage());
        }
    }

    /**
     * 🟠 Sửa ảnh (xóa cũ + upload mới + cập nhật DB)
     */
    public ProductImage updateFile(MultipartFile newFile, ProductImage oldImage) {
        try {
            if (oldImage != null) {
                deleteFile(oldImage.getPublicId());
                imageRepository.delete(oldImage);
            }
            return uploadFile(newFile, oldImage.getProduct());
        } catch (Exception e) {
            throw new RuntimeException("Update file failed: " + e.getMessage());
        }
    }
}
