package com.example.fruitshop_be.vnpay.service;

import com.example.fruitshop_be.enums.Status;
import com.example.fruitshop_be.repository.OrderRepository;
import com.example.fruitshop_be.vnpay.config.Config;
import com.example.fruitshop_be.vnpay.dto.PaymentInfo;
import com.example.fruitshop_be.vnpay.dto.PaymentRequest;
import com.example.fruitshop_be.vnpay.dto.PaymentResDto;
import com.example.fruitshop_be.vnpay.dto.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {

    OrderRepository orderRepository;

    public PaymentResDto createPayment(HttpServletRequest req, PaymentRequest paymentRequest)
            throws UnsupportedEncodingException {

        String orderType = "billpayment";
        long amount = paymentRequest.getAmount() * 100;

        String vnp_TxnRef = paymentRequest.getOrderID();
        String vnp_IpAddr = Config.getIpAddress(req);
        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", Config.vnp_Version);
        vnp_Params.put("vnp_Command", Config.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();

        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;

        return PaymentResDto.builder()
                .status("1000")
                .message("Request payment success")
                .url(paymentUrl)
                .build();
    }

    public boolean handlePaymentInfo(PaymentInfo paymentInfo) {

        String orderID = paymentInfo.getVnp_TxnRef();
        String code = paymentInfo.getVnp_ResponseCode();

        // Thanh toán thành công
        if ("00".equals(code)) {
            var order = orderRepository.findById(orderID).orElse(null);
            if (order != null) {
                orderRepository.save(order);
            }
            return true;
        }

        // Thanh toán thất bại
        var order = orderRepository.findById(orderID).orElse(null);
        if (order != null) {
            orderRepository.delete(order);
        }
        return false;
    }

}