package com.example.fruitshop_be.service;

import com.example.fruitshop_be.dto.request.CartAddRequest;
import com.example.fruitshop_be.dto.request.CartItemUpdateRequest;
import com.example.fruitshop_be.dto.response.CartResponse;
import com.example.fruitshop_be.entity.Cart;
import com.example.fruitshop_be.entity.CartItem;
import com.example.fruitshop_be.entity.CartItemId;
import com.example.fruitshop_be.entity.Customer;
import com.example.fruitshop_be.entity.Product;
import com.example.fruitshop_be.mapper.CartMapper;
import com.example.fruitshop_be.repository.CartItemRepository;
import com.example.fruitshop_be.repository.CartRepository;
import com.example.fruitshop_be.repository.CustomerRepository;
import com.example.fruitshop_be.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final CartMapper cartMapper;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       ProductRepository productRepository,
                       CustomerRepository customerRepository,
                       CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.cartMapper = cartMapper;
    }

    // Lấy giỏ hàng theo customer
    public CartResponse getCartByCustomer(String customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId).orElse(null);
        if (cart == null) return null;
        return cartMapper.toCartResponse(cart);
    }

    // Thêm sản phẩm vào cart
    public CartResponse addToCart(CartAddRequest request) {
        // Lấy customer từ DB
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Lấy cart nếu có
        Cart cart = cartRepository.findByCustomerId(request.getCustomerId()).orElse(null);

        // Nếu chưa có cart, tạo mới
        if (cart == null) {
            cart = new Cart();
            cart.setCustomer(customer);
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (cart.getCartItems() == null) cart.setCartItems(new ArrayList<>());

        // Kiểm tra sản phẩm đã có trong cart chưa
        CartItem existing = cart.getCartItems().stream()
                .filter(ci -> ci.getProduct().getId().equals(request.getProductId()))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + request.getQuantity());
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(request.getQuantity());
            cart.getCartItems().add(item);
        }

        cartRepository.save(cart); // Lưu cart -> tạo cartId
        return cartMapper.toCartResponse(cart);
    }

    // Cập nhật số lượng
    public CartResponse updateCartItem(CartItemUpdateRequest request) {
        // 1. Lấy cart theo customerId
        Cart cart = cartRepository.findByCustomerId(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // 2. Tìm CartItem theo productId trong cart
        CartItem item = cart.getCartItems().stream()
                .filter(ci -> ci.getProduct().getId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        // 3. Cập nhật quantity
        item.setQuantity(request.getQuantity());

        // 4. Lưu cart
        cartRepository.save(cart);

        // 5. Trả về response
        return cartMapper.toCartResponse(cart);
    }


    // Xóa sản phẩm khỏi cart
    public CartResponse removeItem(String customerId, String productId) {
        // 1. Lấy cart theo customerId
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // 2. Tìm CartItem theo productId
        CartItem item = cart.getCartItems().stream()
                .filter(ci -> ci.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        // 3. Xóa CartItem khỏi cart
        cart.getCartItems().remove(item);
        cartItemRepository.delete(item);

        // 4. Lưu cart (không bắt buộc nếu cascade)
        cartRepository.save(cart);

        // 5. Trả về response
        return cartMapper.toCartResponse(cart);
    }

    // Xóa tất cả sản phẩm trong cart
    public CartResponse clearCart(String customerId) {
        // 1. Lấy cart theo customerId
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // 2. Xóa tất cả CartItem
        if (cart.getCartItems() != null && !cart.getCartItems().isEmpty()) {
            cartItemRepository.deleteAll(cart.getCartItems());
            cart.getCartItems().clear();
        }

        // 3. Lưu cart (nếu cần)
        cartRepository.save(cart);

        // 4. Trả về response rỗng
        return cartMapper.toCartResponse(cart);
    }

}
