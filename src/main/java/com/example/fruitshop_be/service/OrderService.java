package com.example.fruitshop_be.service;

import com.example.fruitshop_be.dto.request.OrderCreateRequest;
import com.example.fruitshop_be.dto.request.OrderDetailRequest;
import com.example.fruitshop_be.dto.response.OrderResponse;
import com.example.fruitshop_be.entity.*;
import com.example.fruitshop_be.enums.ErrorCode;
import com.example.fruitshop_be.exception.AppException;
import com.example.fruitshop_be.mapper.OrderDetailMapper;
import com.example.fruitshop_be.mapper.OrderMapper;
import com.example.fruitshop_be.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderService {

    CustomerRepository customerRepository;
    ProductRepository productRepository;
    OrderMapper orderMapper;
    VoucherRepository voucherRepository;
    OrderRepository orderRepository;
    OrderDetailRepository orderDetailRepository;
    OrderDetailMapper orderDetailMapper;

    public synchronized OrderResponse createOrder(OrderCreateRequest request) {

        // STEP 1: validate items
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new AppException(ErrorCode.ORDER_EMPTY_ITEMS);
        }

        // STEP 2: customer
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_CUSTOMER_NOT_FOUND));

        // STEP 3: voucher (optional)
        Voucher voucher = null;
        if (request.getVoucherId() != null) {
            voucher = voucherRepository.findById(request.getVoucherId())
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_VOUCHER_NOT_FOUND));

            // kiểm tra tồn kho voucher
            if (voucher.getQuantity() <= 0) {
                throw new AppException(ErrorCode.VOUCHER_OUT_OF_STOCK);
            }

            // trừ voucher tồn kho
            voucher.setQuantity(voucher.getQuantity() - 1);
            voucherRepository.save(voucher);
        }

        // STEP 4: create order
        Order order = orderMapper.toOrder(request);
        order.setCustomer(customer);
        order.setVoucher(voucher);

        Order savedOrder = orderRepository.save(order);

        // STEP 5: xử lý từng OrderDetail + kiểm tra tồn kho + trừ tồn kho
        List<OrderDetail> details = new ArrayList<>();

        for (OrderDetailRequest item : request.getItems()) {

            if (item.getQuantity() <= 0) {
                throw new AppException(ErrorCode.ORDER_QUANTITY_INVALID);
            }

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_PRODUCT_NOT_FOUND));

            // kiểm tra tồn kho
            if (product.getInventory() <= 0) {
                throw new AppException(ErrorCode.PRODUCT_OUT_OF_STOCK);
            }

            if (product.getInventory() < item.getQuantity()) {
                throw new AppException(ErrorCode.PRODUCT_INSUFFICIENT_QUANTITY);
            }

            // trừ tồn kho sản phẩm
            product.setInventory(product.getInventory() - item.getQuantity());
            productRepository.save(product);

            // tạo OrderDetail
            OrderDetail detail = orderDetailMapper.toOrderDetail(item);
            detail.setOrder(savedOrder);
            detail.setProduct(product);

            details.add(detail);
        }

        orderDetailRepository.saveAll(details);

        // STEP 6: build response
        OrderResponse response = orderMapper.toOrderResponse(savedOrder);

        response.setItems(
                details.stream()
                        .map(orderDetailMapper::toOrderDetailResponse)
                        .toList()
        );

        return response;
    }
}

