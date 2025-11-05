package com.example.fruitshop_be.service;

import com.example.fruitshop_be.dto.request.AuthenticationRequest;
import com.example.fruitshop_be.dto.request.IntrospectRequest;
import com.example.fruitshop_be.dto.response.AuthenticationResponse;
import com.example.fruitshop_be.dto.response.IntrospectResponse;
import com.example.fruitshop_be.entity.Account;
import com.example.fruitshop_be.enums.ErrorCode;
import com.example.fruitshop_be.enums.TokenType;
import com.example.fruitshop_be.exception.AppException;
import com.example.fruitshop_be.repository.AccountRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
                .accessToken(generateToken(account,TokenType.ACCESS))
                .refreshToken(generateToken(account,TokenType.REFRESH))
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
                .claim("scope","ROLE_"+account.getRole().toString())
                .claim("customerID", account.getCustomer().getId())
                .claim("tokenType",tokenType.toString())
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
    public IntrospectResponse introspect(IntrospectRequest resquest) throws JOSEException, ParseException {
        var token = resquest.getToken();
        boolean valid=true;
        try {
            verifyAccessToken(token);
        }catch (AppException e){
            valid=false;
        }
        return  IntrospectResponse.builder()
                .isValid(valid)
                .build();
    }
    private SignedJWT verifySignature(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier jwsVerifier = new MACVerifier(SECRET_KEY.getBytes());

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


}
