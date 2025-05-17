package com.jo2k.garagify.auth.service;

import com.jo2k.dto.TokenDto;
import com.jo2k.garagify.common.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;

    public TokenDto generateToken(String subject) {
        String accessToken = jwtService.generateAccessToken(subject);
        String refreshToken = jwtService.generateRefreshToken(subject);
        return new TokenDto(accessToken, refreshToken);
    }


    public TokenDto getRefreshToken(String refreshToken) {
        if (!jwtService.validateToken(refreshToken)) {
            throw new InvalidTokenException("Invalid token");
        }
        var subject = jwtService.getSubject(refreshToken);
        var type = jwtService.getClaim(refreshToken, "type");
        if (!"refresh".equals(type)) {
            throw new InvalidTokenException("Not a refresh token");
        }

        return generateToken(subject);
    }
}
