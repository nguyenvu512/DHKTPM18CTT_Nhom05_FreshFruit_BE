package com.example.fruitshop_be.service;

import com.example.fruitshop_be.dto.request.AuthenticationRequest;
import com.example.fruitshop_be.dto.request.ForgetPasswordRequest;
import com.example.fruitshop_be.dto.request.IntrospectRequest;
import com.example.fruitshop_be.dto.request.LogoutRequest;
import com.example.fruitshop_be.dto.response.AuthenticationResponse;
import com.example.fruitshop_be.dto.response.IntrospectResponse;
import com.example.fruitshop_be.entity.Account;
import com.example.fruitshop_be.enums.ErrorCode;
import com.example.fruitshop_be.enums.TokenType;
import com.example.fruitshop_be.exception.AppException;
import com.example.fruitshop_be.redis.RedisService;
import com.example.fruitshop_be.repository.AccountRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationService {
    @Value("${jwt.secret}")
    @NonFinal
    String SECRET_KEY;
    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;
    RedisService redisService;
    MailService mailService;
    private static final SecureRandom random = new SecureRandom();

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Account account = accountRepository.findByUsername(request.getUsername());
        if (account == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        boolean authenticated = passwordEncoder.matches(request.getPassword(), account.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.LOGIN_FAILED);
        }
        return AuthenticationResponse.builder()
                .accessToken(generateToken(account, TokenType.ACCESS))
                .refreshToken(generateToken(account, TokenType.REFRESH))
                .build();
    }

    public String generateToken(Account account, TokenType tokenType) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimNames = new JWTClaimsSet.Builder()
                .subject(account.getUsername())
                .issuer("FruitShop.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", "ROLE_" + account.getRole().toString())
                .claim("customerID", account.getCustomer().getId())
                .claim("customerName", account.getCustomer().getName())
                .claim("tokenType", tokenType.toString())
                .build();
        Payload payload = new Payload(jwtClaimNames.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException, AppException {
        var token = request.getToken();
        verifyAccessToken(token);

        return IntrospectResponse.builder()
                .isValid(true)
                .build();
    }

    private SignedJWT verifySignature(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = parseToken(token);
        JWSVerifier jwsVerifier = new MACVerifier(SECRET_KEY.getBytes());
        if (redisService.isBlacklisted(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.INVALIDATED_TOKEN);
        }
        if (!signedJWT.verify(jwsVerifier)) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        return signedJWT;
    }

    public SignedJWT verifyAccessToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifySignature(token);

        String type = (String) signedJWT.getJWTClaimsSet().getClaim("tokenType");
        if (!TokenType.ACCESS.toString().equals(type)) {
            throw new AppException(ErrorCode.INVALID_TOKEN_TYPE);
        }

        if (signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())) {
            throw new AppException(ErrorCode.EXPIRED_TOKEN);
        }

        return signedJWT;
    }

    public SignedJWT verifyRefreshToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifySignature(token);

        String type = (String) signedJWT.getJWTClaimsSet().getClaim("tokenType");
        if (!TokenType.REFRESH.toString().equals(type)) {
            throw new AppException(ErrorCode.INVALID_TOKEN_TYPE);
        }

        if (signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())) {
            throw new AppException(ErrorCode.EXPIRED_TOKEN);
        }

        return signedJWT;
    }

    public void logout(LogoutRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ParseException {
        SignedJWT signedJWT = parseToken(request.getToken());
        String jti = signedJWT.getJWTClaimsSet().getJWTID();

        redisService.blacklistToken(jti);

        Cookie[] cookies = httpRequest.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    cookie.setHttpOnly(true);
                    httpResponse.addCookie(cookie);
                }
            }
        }
    }

    private SignedJWT parseToken(String token) throws ParseException {
        return SignedJWT.parse(token);
    }
    public void forgetPassword(ForgetPasswordRequest request) {
        Account account = accountRepository.findByUsername(request.getUsername());
        if (account == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        String newPassword = randomForgetPassword();
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
        mailService.sendResetPasswordMail(request.getUsername(),newPassword);

    }
    public static String randomForgetPassword() {
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10)); // 0–9
        }
        return sb.toString();
    }


}
