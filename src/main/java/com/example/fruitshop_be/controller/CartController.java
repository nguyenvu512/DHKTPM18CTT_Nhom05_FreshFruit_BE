package com.example.fruitshop_be.controller;

import com.example.fruitshop_be.dto.ApiResponse;
import com.example.fruitshop_be.dto.request.CartCreateRequest;
import com.example.fruitshop_be.dto.request.CartItemRequest;
import com.example.fruitshop_be.dto.request.CartUpdateRequest;
import com.example.fruitshop_be.dto.response.CartResponse;
import com.example.fruitshop_be.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CartController {

    CartService cartService;

    // ✅ Tạo giỏ hàng mới
    @PostMapping
    public ApiResponse<CartResponse> createCart(@RequestBody CartCreateRequest request) {
        return ApiResponse.<CartResponse>builder()
                .result(cartService.createCart(request))
                .build();
    }

    // ✅ Lấy giỏ hàng theo customerId
    @GetMapping("/customer/{customerId}")
    public ApiResponse<CartResponse> getCartByCustomer(@PathVariable String customerId) {
        return ApiResponse.<CartResponse>builder()
                .result(cartService.getCartByCustomer(customerId))
                .build();
    }

    // ✅ Lấy giỏ hàng theo cartId
    @GetMapping("/{cartId}")
    public ApiResponse<CartResponse> getCartById(@PathVariable String cartId) {
        return ApiResponse.<CartResponse>builder()
                .result(cartService.getCartById(cartId))
                .build();
    }


    // ✅ Xóa toàn bộ giỏ hàng theo customerId
    @DeleteMapping("/customer/{customerId}")
    public ApiResponse<Void> clearCart(@PathVariable String customerId) {
        cartService.clearCart(customerId);
        return ApiResponse.<Void>builder()
                .message("Cart cleared successfully")
                .build();
    }

    // ✅ Thêm sản phẩm vào giỏ hàng theo cartId
    @PostMapping("/{cartId}/add-item")
    public ApiResponse<CartResponse> addItemToCartByCartId(
            @PathVariable String cartId,
            @RequestBody CartItemRequest request) {
        return ApiResponse.<CartResponse>builder()
                .result(cartService.addItemToCartByCartId(cartId, request))
                .build();
    }

    // ✅ Xóa 1 sản phẩm trong giỏ hàng theo cartId + productId
    @DeleteMapping("/{cartId}/remove-item/{productId}")
    public ApiResponse<CartResponse> removeItemByCartId(
            @PathVariable String cartId,
            @PathVariable String productId) {
        return ApiResponse.<CartResponse>builder()
                .result(cartService.removeItemByCartId(cartId, productId))
                .build();
    }

    // ✅ Cập nhật số lượng sản phẩm trong giỏ hàng
    @PutMapping("/{cartId}/update-quantity/{productId}")
    public ApiResponse<CartResponse> updateItemQuantity(
            @PathVariable String cartId,
            @PathVariable String productId,
            @RequestParam int quantity) {

        // Tạo request cho service
        CartUpdateRequest request = new CartUpdateRequest();
        request.setCartId(cartId);
        request.setProductId(productId);
        request.setQuantity(quantity);

        return ApiResponse.<CartResponse>builder()
                .result(cartService.updateItemQuantity(request))
                .build();
    }


}
