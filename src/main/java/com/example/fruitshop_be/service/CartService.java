package com.example.fruitshop_be.service;

import com.example.fruitshop_be.dto.request.CartCreateRequest;
import com.example.fruitshop_be.dto.request.CartUpdateRequest;
import com.example.fruitshop_be.dto.request.CartItemRequest;
import com.example.fruitshop_be.dto.response.CartResponse;
import com.example.fruitshop_be.entity.*;
import com.example.fruitshop_be.enums.ErrorCode;
import com.example.fruitshop_be.exception.AppException;
import com.example.fruitshop_be.mapper.CartMapper;
import com.example.fruitshop_be.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CartService {

    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    CustomerRepository customerRepository;
    ProductRepository productRepository;
    CartMapper cartMapper;

    public CartResponse createCart(CartCreateRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart.setCreateAt(LocalDateTime.now());
        cart.setUpdateAt(LocalDateTime.now());

        List<CartItem> cartItems = request.getCartItems().stream().map(itemReq -> {
            Product product = productRepository.findById(itemReq.getProductID())
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(itemReq.getQuantity());
            return cartItem;
        }).collect(Collectors.toList());

        cart.setCartItems(cartItems);
        cartRepository.save(cart);
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse getCartByCustomer(String customerId) {
        Cart cart = cartRepository.findByCustomer_Id(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        return cartMapper.toCartResponse(cart);
    }

    public void clearCart(String customerId) {
        Cart cart = cartRepository.findByCustomer_Id(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        cart.getCartItems().clear();
        cart.setUpdateAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

    public CartResponse addItemToCartByCartId(String cartId, CartItemRequest request) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        Product product = productRepository.findById(request.getProductID())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        if (cart.getCartItems() == null) {
            cart.setCartItems(new java.util.ArrayList<>());
        }

        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            cart.getCartItems().add(newItem);
        }

        cart.setUpdateAt(LocalDateTime.now());
        cartRepository.save(cart);
        return cartMapper.toCartResponse(cart);
    }

    // ✅ Lấy giỏ hàng theo cartId
    public CartResponse getCartById(String cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        return cartMapper.toCartResponse(cart);
    }

    // ✅ Xóa sản phẩm theo cartId
    public CartResponse removeItemByCartId(String cartId, String productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        cart.getCartItems().removeIf(item -> item.getProduct().getId().equals(productId));
        cart.setUpdateAt(LocalDateTime.now());
        cartRepository.save(cart);

        return cartMapper.toCartResponse(cart);
    }

    // ✅ Cập nhật số lượng sản phẩm trong giỏ hàng theo cartId + productId
    public CartResponse updateItemQuantity(CartUpdateRequest request) {
        // Lấy giỏ hàng theo cartId
        Cart cart = cartRepository.findById(request.getCartId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        // Tìm item trong giỏ
        CartItem item = cart.getCartItems().stream()
                .filter(i -> i.getProduct().getId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        // Cập nhật số lượng
        item.setQuantity(request.getQuantity());

        // Cập nhật thời gian sửa giỏ
        cart.setUpdateAt(LocalDateTime.now());

        // Lưu giỏ hàng
        cartRepository.save(cart);

        // Trả về CartResponse
        return cartMapper.toCartResponse(cart);
    }


}
