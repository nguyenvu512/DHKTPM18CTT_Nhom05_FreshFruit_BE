package com.example.fruitshop_be.controller;

import com.example.fruitshop_be.dto.ApiResponse;
import com.example.fruitshop_be.dto.request.AuthenticationRequest;
import com.example.fruitshop_be.dto.request.ForgetPasswordRequest;
import com.example.fruitshop_be.dto.request.IntrospectRequest;
import com.example.fruitshop_be.dto.request.LogoutRequest;
import com.example.fruitshop_be.dto.response.AccountResponse;
import com.example.fruitshop_be.dto.response.AuthenticationResponse;
import com.example.fruitshop_be.dto.response.IntrospectResponse;
import com.example.fruitshop_be.entity.Account;
import com.example.fruitshop_be.enums.ErrorCode;
import com.example.fruitshop_be.enums.TokenType;
import com.example.fruitshop_be.exception.AppException;
import com.example.fruitshop_be.service.AccountService;
import com.example.fruitshop_be.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    AuthenticationService authenticationService;
    AccountService accountService;

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
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .result(authenticationService.introspect(request))
                .build();
    }
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResonse) throws ParseException, JOSEException {
        authenticationService.logout(request, httpRequest, httpResonse);
        return ApiResponse.<Void>builder()
                .message("Logout successful")
                .build();
    }
    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refreshToken(HttpServletRequest request) throws ParseException, JOSEException {
        String refreshToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if (refreshToken == null) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        SignedJWT signedJWT = authenticationService.verifyRefreshToken(refreshToken);
        if (signedJWT == null) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        Account account = accountService.getAccountByUserName(signedJWT.getJWTClaimsSet().getSubject());
        String token = authenticationService.generateToken(account, TokenType.ACCESS);
        AuthenticationResponse.builder().accessToken(token).build();
        return ApiResponse.<AuthenticationResponse>builder()
                .result(AuthenticationResponse.builder().accessToken(token).build())
                .build();

    }
    @PostMapping("/forget-password")
    public ApiResponse<Void> forgetPassword(@RequestBody ForgetPasswordRequest request){
        authenticationService.forgetPassword(request);
        return ApiResponse.<Void>builder()
                .message("New password has been send to your email")
                .build();
    }
    @GetMapping("/debug")
    public String debugAuth(Authentication authentication) {

        System.out.println("✅ authentication = " + authentication);
        System.out.println("✅ authentication.getName() = " + authentication.getName());
        System.out.println("✅ authentication.getPrincipal() = " + authentication.getPrincipal());
        System.out.println("✅ authorities = " + authentication.getAuthorities());

        return "Check console log";
    }

}

