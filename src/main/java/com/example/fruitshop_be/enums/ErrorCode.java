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
    LOGIN_FAILED(1401, "Invalid username or password", HttpStatus.UNAUTHORIZED);
    int code;
    String message;
    HttpStatus httpStatus;
}
