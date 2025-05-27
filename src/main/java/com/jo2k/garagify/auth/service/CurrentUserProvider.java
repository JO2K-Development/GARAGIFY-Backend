package com.jo2k.garagify.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final JwtService jwtService;
    private final HttpServletRequest request;

    public UUID getCurrentUserId() {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return UUID.fromString(jwtService.getSubject(token));
        }
        throw new IllegalStateException("Missing or invalid Authorization header");
    }
}