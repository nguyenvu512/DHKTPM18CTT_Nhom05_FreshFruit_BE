package com.example.fruitshop_be.controller;

import com.example.fruitshop_be.dto.ApiResponse;
import com.example.fruitshop_be.dto.request.AuthenticationRequest;
import com.example.fruitshop_be.dto.response.AuthenticationResponse;
import com.example.fruitshop_be.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody AuthenticationRequest authenticationRequest) {
        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", response.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
        response.setRefreshToken(null);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.<AuthenticationResponse>builder()
                        .result(response)
                        .build());
    }
}

