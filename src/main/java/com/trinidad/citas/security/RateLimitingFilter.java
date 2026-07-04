package com.trinidad.citas.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.local.LocalBucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Filtro de rate limiting basado en Bucket4j (token bucket).
 * <p>
 * Se registra en la cadena de Spring Security DESPUES del filtro JWT,
 * por lo que puede distinguir entre usuarios autenticados y anonimos.
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

    private final Map<String, LocalBucket> buckets = new ConcurrentHashMap<>();

    private static final Bandwidth AUTH_LIMIT      = Bandwidth.classic(5,   Refill.intervally(5,   Duration.ofMinutes(1)));
    private static final Bandwidth USER_LIMIT      = Bandwidth.classic(120, Refill.intervally(120, Duration.ofMinutes(1)));
    private static final Bandwidth ANONYMOUS_LIMIT = Bandwidth.classic(30,  Refill.intervally(30,  Duration.ofMinutes(1)));

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

        String clientKey = resolveClientKey(request, path);
        Bandwidth limit = resolveLimit(path);
        LocalBucket bucket = buckets.computeIfAbsent(clientKey,
                k -> Bucket.builder().addLimit(limit).build());

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

    private String resolveClientKey(HttpServletRequest request, String path) {
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
