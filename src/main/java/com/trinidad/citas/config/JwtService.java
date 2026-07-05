package com.trinidad.citas.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    /** Tamaño mínimo recomendado para HMAC-SHA256: 256 bits = 32 bytes */
    private static final int MIN_SECRET_BYTES = 32;

    @Value("${trinidad.jwt.secret}")
    private String secret;

    @Value("${trinidad.jwt.expiration-ms}")
    private long expirationMs;

    /**
     * Valida al iniciar que el JWT secret tenga al menos 256 bits (32 bytes).
     * HMAC-SHA256 requiere una clave de al menos 256 bits.
     */
    @PostConstruct
    public void validarSecret() {
        try {
            getKey();
            log.info("JWT secret configurado correctamente (min. {} bytes requeridos)", MIN_SECRET_BYTES);
        } catch (Exception e) {
            log.error("JWT SECRET INVALIDO: {} — Debe tener al menos {} bytes (256 bits) " +
                      "o ser un Base64 válido. Ej: openssl rand -base64 32", e.getMessage(), MIN_SECRET_BYTES);
            throw new IllegalStateException(
                "JWT_SECRET invalido o demasiado corto. Se requieren al menos " +
                MIN_SECRET_BYTES + " bytes (256 bits) para HMAC-SHA256", e);
        }
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
            .map(a -> a.getAuthority()).toList());
        return Jwts.builder()
            .claims(claims)
            .subject(userDetails.getUsername())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expirationMs))
            .signWith(getKey())
            .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser()
            .verifyWith(getKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
        return resolver.apply(claims);
    }

    private SecretKey getKey() {
        byte[] keyBytes = secret.length() >= 64
            ? secret.getBytes()
            : Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
