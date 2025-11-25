package com.example.fruitshop_be.controller;

import com.example.fruitshop_be.dto.request.CartAddRequest;
import com.example.fruitshop_be.dto.request.CartItemUpdateRequest;
import com.example.fruitshop_be.dto.response.CartResponse;
import com.example.fruitshop_be.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Lấy giỏ hàng
    @GetMapping("/{customerId}")
    public CartResponse getCart(@PathVariable String customerId) {
        return cartService.getCartByCustomer(customerId);
    }

    // Thêm sản phẩm
    @PostMapping("/add")
    public CartResponse addToCart(@RequestBody CartAddRequest request) {
        return cartService.addToCart(request);
    }

    // Cập nhật số lượng
    @PutMapping("/update")
    public CartResponse updateCartItem(@RequestBody CartItemUpdateRequest request) {
        return cartService.updateCartItem(request);
    }

    // Xóa sản phẩm
    @DeleteMapping("/remove/{customerId}/{productId}")
    public CartResponse removeItem(@PathVariable String customerId,
                                   @PathVariable String productId) {
        return cartService.removeItem(customerId, productId);
    }

    // Xóa tất cả sản phẩm trong cart
    @DeleteMapping("/clear/{customerId}")
    public CartResponse clearCart(@PathVariable String customerId) {
        return cartService.clearCart(customerId);
    }

}
