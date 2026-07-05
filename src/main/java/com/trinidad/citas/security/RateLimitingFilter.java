package com.trinidad.citas.security;

import java.io.IOException;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro de rate limiting basado en Bucket4j (token bucket).
 * <p>
 * Se registra en la cadena de Spring Security DESPUES del filtro JWT,
 * por lo que puede distinguir entre usuarios autenticados y anonimos.
 * <p>
 * Usa Caffeine Cache con expiracion automatica para evitar memory leaks:
 * los buckets de IPs/usurios inactivos se eliminan automaticamente
 * despues de 10 minutos sin actividad.
 * <p>
 * Limites:
 * <ul>
 *   <li><b>Auth</b> (/api/auth/**) — 5 requests/minuto (mitiga fuerza bruta)</li>
 *   <li><b>API autenticada</b> — 120 requests/minuto por usuario</li>
 *   <li><b>API anonima</b> — 30 requests/minuto por IP</li>
 * </ul>
 */
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RateLimitingFilter.class);

    /**
     * Cache de buckets con expiracion por falta de uso (10 min).
     * Esto evita el memory leak: buckets inactivos se eliminan automaticamente.
     */
    private final Cache<String, Bucket> bucketCache = Caffeine.newBuilder()
        .expireAfterAccess(Duration.ofMinutes(10))
        .maximumSize(10_000)
        .build();

    private static final Bandwidth AUTH_LIMIT      = Bandwidth.builder().capacity(5).refillIntervally(5, Duration.ofMinutes(1)).build();
    private static final Bandwidth USER_LIMIT      = Bandwidth.builder().capacity(120).refillIntervally(120, Duration.ofMinutes(1)).build();
    private static final Bandwidth ANONYMOUS_LIMIT = Bandwidth.builder().capacity(30).refillIntervally(30, Duration.ofMinutes(1)).build();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Excluir Swagger y recursos estaticos
        if (isStaticResource(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientKey = resolveClientKey(request);
        Bandwidth limit = resolveLimit(path);

        // Obtener o crear bucket desde la cache (se limpia solo al expirar)
        Bucket bucket = bucketCache.get(clientKey, k -> Bucket.builder().addLimit(limit).build());

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            log.warn("Rate limit excedido para {} en {}", clientKey, path);
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("""
                {"error":"too_many_requests","message":"Demasiadas solicitudes. Intente de nuevo en 1 minuto."}
                """.trim());
        }
    }

    private boolean isStaticResource(String path) {
        return path.contains("/swagger-ui") || path.contains("/v3/api-docs")
            || path.contains("/swagger-resources") || path.contains("/webjars/")
            || path.contains("/css/") || path.contains("/js/") || path.contains("/img/")
            || path.contains("/h2-console");
    }

    private String resolveClientKey(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal())) {
            return "u:" + auth.getName();
        }
        return "ip:" + obtenerIp(request);
    }

    private Bandwidth resolveLimit(String path) {
        if (path.contains("/api/auth/") || path.contains("/api/v1/auth/")) {
            return AUTH_LIMIT;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal())) {
            return USER_LIMIT;
        }
        return ANONYMOUS_LIMIT;
    }

    private String obtenerIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        } else {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
