package com.amerkhaled.blogplatform.services.impl;

import com.amerkhaled.blogplatform.services.AuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    private final Long jwtExpiryMs = 86400000L; // 1 day

    @Override
    public UserDetails authenticate(String email, String password) {
        log.info("Authenticating user with email: {}", email);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            log.info("Authentication successful for user: {}", email);
            return userDetails;
        } catch (BadCredentialsException ex) {
            log.warn("Authentication failed for user: {} — Invalid credentials", email);
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error during authentication for user: {} — {}", email, ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        log.info("Generating JWT token for user: {}", userDetails.getUsername());

        try {
            Map<String, Object> claims = new HashMap<>();

            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiryMs))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();

            log.debug("Generated JWT token for user {}: {}", userDetails.getUsername(), token);
            return token;
        } catch (Exception ex) {
            log.error("Failed to generate JWT token for user: {} — {}", userDetails.getUsername(), ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public UserDetails validateToken(String token) {
        log.info("Validating JWT token");
        try {
            String username = extractUsername(token);
            log.debug("Extracted username from token: {}", username);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            log.info("Token validated successfully for user: {}", username);
            return userDetails;
        } catch (Exception ex) {
            log.warn("Invalid or expired JWT token — {}", ex.getMessage());
            throw ex;
        }
    }

    private String extractUsername(String token) {
        try {
            log.debug("Extracting username from JWT token");
            Claims claims = Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            log.debug("JWT claims extracted successfully");
            return claims.getSubject();
        } catch (Exception ex) {
            log.error("Failed to extract username from token — {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    private Key getSigningKey() {
        try {
            log.debug("Generating signing key from secret");
            byte[] keyBytes = secretKey.getBytes();
            Key key = Keys.hmacShaKeyFor(keyBytes);
            log.debug("Signing key generated successfully ({} bytes)", keyBytes.length);
            return key;
        } catch (Exception ex) {
            log.error("Failed to generate signing key — {}", ex.getMessage(), ex);
            throw ex;
        }
    }
}
