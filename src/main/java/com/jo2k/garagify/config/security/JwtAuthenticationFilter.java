package com.jo2k.garagify.config.security;

import com.jo2k.garagify.auth.service.JwtService;
import com.jo2k.garagify.common.exception.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) {
        try {

            final String authHeader = request.getHeader("Authorization");
            final String token;

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            token = authHeader.substring(7);

            if (jwtService.validateToken(token)) {
                String userId = jwtService.getSubject(token);

                var authToken = new UsernamePasswordAuthenticationToken(userId, null, null);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            filterChain.doFilter(request, response);
        } catch (InvalidTokenException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}