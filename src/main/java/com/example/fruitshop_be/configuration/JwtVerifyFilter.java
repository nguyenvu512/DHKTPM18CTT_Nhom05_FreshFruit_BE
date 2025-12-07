package com.example.fruitshop_be.configuration;

import com.example.fruitshop_be.dto.ApiResponse;
import com.example.fruitshop_be.exception.AppException;
import com.example.fruitshop_be.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtVerifyFilter extends OncePerRequestFilter {

    @Autowired
    @Lazy
    private AuthenticationService authenticationService;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                // Kiểm tra token theo custom logic
                authenticationService.verifyAccessToken(token);
            } catch (AppException ex) {
                // Trả JSON ngay lập tức
                response.setStatus(ex.getErrorCode().getHttpStatus().value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                ObjectMapper objectMapper = new ObjectMapper();
                response.getWriter().write(objectMapper.writeValueAsString(
                        ApiResponse.builder()
                                .code(ex.getErrorCode().getCode())
                                .message(ex.getErrorCode().getMessage())
                                .build()
                ));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}