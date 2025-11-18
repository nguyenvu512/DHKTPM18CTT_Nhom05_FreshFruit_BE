package com.example.fruitshop_be.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public enum ErrorCode {

    CUSTOMER_EXISTS(1409, "Customer email already exists", HttpStatus.CONFLICT),
    NOT_FOUND(1404,"Resource not found",HttpStatus.NOT_FOUND),
    INVALID_TOKEN(1405, "Token is invalid", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN(1406, "Token is expired", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN_TYPE(1407, "Invalid token type", HttpStatus.UNAUTHORIZED),
    INVALIDATED_TOKEN(1408, "Token has been invalidated", HttpStatus.UNAUTHORIZED),
    LOGIN_FAILED(1401, "Invalid username or password", HttpStatus.UNAUTHORIZED),
    ORDER_CUSTOMER_NOT_FOUND(2401, "Customer not found", HttpStatus.NOT_FOUND),
    ORDER_VOUCHER_NOT_FOUND(2402, "Voucher not found", HttpStatus.NOT_FOUND),
    ORDER_PRODUCT_NOT_FOUND(2403, "Product not found", HttpStatus.NOT_FOUND),
    ORDER_EMPTY_ITEMS(2404, "Order has no items", HttpStatus.BAD_REQUEST),
    ORDER_QUANTITY_INVALID(2405, "Quantity must be > 0", HttpStatus.BAD_REQUEST),
    PRODUCT_OUT_OF_STOCK(2501, "Product is out of stock", HttpStatus.BAD_REQUEST),
    PRODUCT_INSUFFICIENT_QUANTITY(2502, "Not enough product inventory", HttpStatus.BAD_REQUEST),
    VOUCHER_OUT_OF_STOCK(2503, "Voucher is out of stock", HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatus httpStatus;
}
