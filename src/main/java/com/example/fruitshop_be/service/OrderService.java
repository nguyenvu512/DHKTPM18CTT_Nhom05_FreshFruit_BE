package com.example.fruitshop_be.service;

import com.example.fruitshop_be.dto.request.OrderCreateRequest;
import com.example.fruitshop_be.dto.request.OrderDetailRequest;
import com.example.fruitshop_be.dto.response.OrderResponse;
import com.example.fruitshop_be.entity.*;
import com.example.fruitshop_be.enums.ErrorCode;
import com.example.fruitshop_be.enums.Payment;
import com.example.fruitshop_be.enums.Status;
import com.example.fruitshop_be.exception.AppException;
import com.example.fruitshop_be.mapper.OrderDetailMapper;
import com.example.fruitshop_be.mapper.OrderMapper;
import com.example.fruitshop_be.repository.*;
import com.example.fruitshop_be.vnpay.dto.PaymentRequest;
import com.example.fruitshop_be.vnpay.dto.PaymentResDto;
import com.example.fruitshop_be.vnpay.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
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
    CartService cartService;
    PaymentService paymentService;

    public List<OrderResponse> getAllOrders() {

        List<Order> orders = orderRepository.findAll();

        List<OrderResponse> responses = new ArrayList<>();

        for (Order order : orders) {
            OrderResponse res = orderMapper.toOrderResponse(order);

            // load items
            List<OrderDetail> details = orderDetailRepository.findByOrder(order);

            res.setItems(
                    details.stream()
                            .map(orderDetailMapper::toOrderDetailResponse)
                            .toList()
            );

            responses.add(res);
        }

        return responses;
    }

    public List<OrderResponse> getOrdersByCustomer(String customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_CUSTOMER_NOT_FOUND));

        List<Order> orders = orderRepository.findByCustomer(customer);

        List<OrderResponse> responses = new ArrayList<>();

        for (Order order : orders) {

            OrderResponse res = orderMapper.toOrderResponse(order);

            List<OrderDetail> details = orderDetailRepository.findByOrder(order);

            res.setItems(
                    details.stream()
                            .map(orderDetailMapper::toOrderDetailResponse)
                            .toList()
            );

            responses.add(res);
        }

        return responses;
    }



    public synchronized OrderResponse createOrder(OrderCreateRequest request, HttpServletRequest httpRequest) throws UnsupportedEncodingException {

    // STEP 1: validate items
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new AppException(ErrorCode.ORDER_EMPTY_ITEMS);
        }

        // STEP 2: customer
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_CUSTOMER_NOT_FOUND));

        // STEP 3: voucher (optional)
        Voucher voucher = null;
        if (request.getVoucherId() != null && !request.getVoucherId().toString().isBlank()) {
            voucher = voucherRepository.findById(request.getVoucherId())
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_VOUCHER_NOT_FOUND));

            if (voucher.getQuantity() <= 0) {
                throw new AppException(ErrorCode.VOUCHER_OUT_OF_STOCK);
            }

            voucher.setQuantity(voucher.getQuantity() - 1);
            voucherRepository.save(voucher);
        }


        // STEP 4: create order
        Order order = orderMapper.toOrder(request);
        order.setCustomer(customer);
        order.setVoucher(voucher);

        if (request.getPaymentMethod() == Payment.VN_PAY) {
            order.setStatus(Status.PENDING); // Chờ thanh toán
        } else {
            order.setStatus(Status.COMPLETED); // COD hoặc các phương thức khác
        }
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

//        clear card
        cartService.clearCart(customer.getId());

        // STEP 6: build response
        OrderResponse response = orderMapper.toOrderResponse(savedOrder);

        response.setItems(
                details.stream()
                        .map(orderDetailMapper::toOrderDetailResponse)
                        .toList()
        );

        // STEP 7: Nếu payment method là VN_PAY, tạo payment URL
        if (request.getPaymentMethod() == Payment.VN_PAY) {
            PaymentRequest paymentRequest = PaymentRequest.builder()
                    .orderID(savedOrder.getId().toString())
                    .amount(request.getTotalAmount().longValue())
                    .build();

            PaymentResDto paymentResDto = paymentService.createPayment(httpRequest, paymentRequest);
            response.setPaymentUrl(paymentResDto.getUrl());
            response.setPaymentStatus(paymentResDto.getStatus());
        }

        return response;
    }
}

