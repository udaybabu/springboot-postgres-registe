package com.example.springboot_postgres_register.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

public class JwtUtil {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Generate JWT with 10 minutes expiration
    public static String generateToken(String email) {
        long expirationTimeMillis = 10 * 60 * 1000; // 10 minutes

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
                .signWith(SECRET_KEY)
                .compact();
    }

    // ✅ Combined method — safely validates & returns email, or null if invalid
    public static String getEmailFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            // Token is expired, invalid, or malformed
            return null;
        }
    }
}
