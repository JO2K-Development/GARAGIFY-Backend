package com.jo2k.garagify.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.jo2k.garagify.common.exception.InvalidTokenException;
import com.jo2k.garagify.config.google.GoogleProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class GoogleTokenVerifierService {

    private final GoogleProperties googleProperties;

    public GoogleIdToken.Payload verifyToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory())
                    .setAudience(Collections.singletonList(googleProperties.getClientId()))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                return idToken.getPayload();
            } else {
                throw new InvalidTokenException("Invalid ID token.");
            }
        } catch (Exception e) {
            throw new InvalidTokenException("Token verification failed");
        }
    }

    public String verifyTokenMock(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                throw new IllegalArgumentException("Invalid JWT");
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> claims = objectMapper.readValue(payloadJson, Map.class);

            return (String) claims.get("email");
        } catch (Exception e) {
            throw new InvalidTokenException("Failed to decode JWT: " + e.getMessage());
        }
    }
}