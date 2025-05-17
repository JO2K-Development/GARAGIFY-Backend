package com.jo2k.garagify.auth.service;

import com.jo2k.garagify.config.jwt.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class JwtService {
    private final JwtProperties properties;
    private static final long ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 7; // 7 days
    private static final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 30; // 30 days

    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(properties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String subject) {
        return Jwts.builder()
                .claims()
                .add("sub", subject)
                .add("type", "access")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .and()
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(String subject) {
        return Jwts.builder()
                .claims()
                .add("sub", subject)
                .add("type", "refresh")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .and()
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getSubject(String token) {
        return getAllClaims(token).get("sub", String.class);
    }

    public String getClaim(String token, String key) {
        return getAllClaims(token).get(key, String.class);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
