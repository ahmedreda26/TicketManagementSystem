package com.example.user_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long ttlMillis;

    public JwtTokenProvider(
            @Value("${jwt.secret-base64}") String secretB64,
            @Value("${jwt.expiration-ms:28800000}") long ttlMillis
    ) {
        if (secretB64 == null || secretB64.isBlank()) {
            throw new IllegalStateException("jwt.secret-base64 is missing. Set env JWT_SECRET_B64.");
        }
        byte[] bytes = Decoders.BASE64.decode(secretB64.trim()); // decode Base64 text
        if (bytes.length < 32) {
            throw new IllegalStateException("JWT secret too short. Provide >= 32 bytes (Base64).");
        }
        this.key = Keys.hmacShaKeyFor(bytes); // HS256/384/512-capable
        this.ttlMillis = ttlMillis;
    }

    public String generateToken(String username, String role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(ttlMillis)))
                .signWith(key, Jwts.SIG.HS512)   // keep HS512
                .compact();
    }

    private Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key)                 // same key as signing
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsernameFromToken(String token) {
        return parse(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return parse(token).get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }
}
