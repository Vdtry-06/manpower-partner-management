package com.vdtry06.partner_management.source.server.config.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
@Getter
public class JwtProvider {
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token.expiration}")
    private long expirationTime;

    @Value("${jwt.refresh-token.expiration}")
    private long expirationRefreshTime;

    private final String AUTH_PREFIX = "Bearer ";
    private final String HEADER = "Authorization";

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issueTime(now)
                .expirationTime(expiryDate)
                .jwtID(UUID.randomUUID().toString()) // Thêm JWT ID
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        try {
            signedJWT.sign(new MACSigner(secretKey.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        } catch (JOSEException e) {
            log.error("Lỗi tạo token: {}", e.getMessage());
            throw new RuntimeException("Không thể tạo JWT token", e);
        }
        return signedJWT.serialize();
    }

    public String getUsernameFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            log.error("Lỗi parse token: {}", e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.verify(new MACVerifier(secretKey.getBytes(java.nio.charset.StandardCharsets.UTF_8))) &&
                    !signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date());
        } catch (ParseException | JOSEException e) {
            log.error("Token không hợp lệ: {}", e.getMessage());
            return false;
        }
    }

    public JWTClaimsSet parseClaims(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            signedJWT.verify(new MACVerifier(secretKey.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
            return signedJWT.getJWTClaimsSet();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi parse claims: " + e.getMessage(), e);
        }
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTH_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // Thêm method lấy JWT ID
    public String getJwtIdFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getJWTID();
        } catch (ParseException e) {
            log.error("Lỗi parse token: {}", e.getMessage());
            return null;
        }
    }
}