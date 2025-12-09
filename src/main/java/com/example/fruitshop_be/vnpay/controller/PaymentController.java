package com.example.fruitshop_be.vnpay.controller;

import com.example.fruitshop_be.dto.ApiResponse;
import com.example.fruitshop_be.vnpay.config.Config;
import com.example.fruitshop_be.vnpay.dto.PaymentInfo;
import com.example.fruitshop_be.vnpay.dto.PaymentRequest;
import com.example.fruitshop_be.vnpay.dto.PaymentResDto;
import com.example.fruitshop_be.vnpay.dto.PaymentResponse;
import com.example.fruitshop_be.vnpay.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @GetMapping("/payment-info")
    public void paymentInfo(
            @RequestParam String vnp_Amount,
            @RequestParam String vnp_BankCode,
            @RequestParam String vnp_TxnRef,
            @RequestParam String vnp_CardType,
            @RequestParam String vnp_ResponseCode,
            HttpServletResponse response
    ) throws IOException {

        PaymentInfo paymentInfo = PaymentInfo.builder()
                .vnp_Amount(vnp_Amount)
                .vnp_BankCode(vnp_BankCode)
                .vnp_TxnRef(vnp_TxnRef)
                .vnp_CardType(vnp_CardType)
                .vnp_ResponseCode(vnp_ResponseCode)
                .build();

        boolean paymentSuccess = paymentService.handlePaymentInfo(
                PaymentInfo.builder()
                        .vnp_Amount(vnp_Amount)
                        .vnp_BankCode(vnp_BankCode)
                        .vnp_TxnRef(vnp_TxnRef)
                        .vnp_CardType(vnp_CardType)
                        .vnp_ResponseCode(vnp_ResponseCode)
                        .build()
        );

        if (paymentSuccess) {
            response.sendRedirect("http://localhost:5173/payment-result?status=success");
        } else {
            response.sendRedirect("http://localhost:5173/payment-result?status=fail");
        }
    }

}