package com.trinidad.citas.service;

import com.trinidad.citas.config.JwtService;
import com.trinidad.citas.dto.LoginRequest;
import com.trinidad.citas.dto.LoginResponse;
import com.trinidad.citas.model.Usuario;
import com.trinidad.citas.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Servicio de autenticación para la API REST.
 *
 * Aquí validamos el login de los usuarios que vienen desde la app móvil
 * o desde el frontend que consume la API con JWT.
 *
 * Cosas importantes:
 *  - Nunca decimos "usuario no existe" vs "contraseña incorrecta"
 *    para que un atacante no pueda saber qué usuarios están registrados.
 *  - Si alguien falla muchas veces, se bloquea la cuenta.
 *  - Cuando el login es exitoso, reseteamos los intentos fallidos.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final IntentoLoginService intentoLoginService;

    /**
     * Intenta iniciar sesión con username y contraseña.
     * Si funciona, devuelve un token JWT.
     * Si no, registra el intento fallido y posiblemente bloquea la cuenta.
     */
    public LoginResponse login(LoginRequest request) {
        String ip = obtenerIp();
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername()).orElse(null);

        // ⚠ OJO: aquí NO diferenciamos entre "usuario no existe",
        // "cuenta bloqueada" o "contraseña incorrecta".
        // Siempre decimos "Credenciales inválidas" por seguridad.
        // Si no, un hacker podría saber qué usuarios existen en el sistema.
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception e) {
            // Login falló. Registramos el intento.
            intentoLoginService.registrarIntento(request.getUsername(), false, ip, "Credenciales invalidas");
            if (usuario != null) {
                usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);
                // Si ya van 5 o más en 30 min, bloqueamos la cuenta
                if (intentoLoginService.debeBloquear(request.getUsername())) {
                    usuario.setBloqueado(1);
                    usuarioRepository.save(usuario);
                }
            }
            // Lanzamos excepción genérica
            throw new BadCredentialsException("Credenciales invalidas");
        }

        // Si llegamos aquí, el login fue exitoso.
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails);

        // Actualizamos datos del usuario: último login, reseteamos intentos fallidos
        usuario = usuarioRepository.findByUsername(request.getUsername()).orElseThrow();
        usuario.setFechaUltLogin(LocalDateTime.now());
        usuario.setIntentosFallidos(0);
        // Por si acaso estaba bloqueado (auto-desbloqueo al acertar la contraseña)
        if (usuario.isBloqueado()) {
            usuario.setBloqueado(0);
        }
        usuarioRepository.save(usuario);

        // Registramos el intento exitoso (para auditoría)
        intentoLoginService.registrarIntento(request.getUsername(), true, ip, null);

        // Devolvemos el token + datos básicos del usuario
        return LoginResponse.builder()
            .token(token)
            .tipo("Bearer")
            .username(usuario.getUsername())
            .email(usuario.getEmail())
            .roles(usuario.getRoles().stream().map(r -> r.getNombre()).collect(Collectors.toSet()))
            .expiracionMs(jwtService.getExpirationMs())
            .build();
    }

    /**
     * Obtiene la IP del cliente. Si viene detrás de un proxy (NGINX, Cloudflare, etc.),
     * usa el header X-Forwarded-For para obtener la IP real.
     */
    private String obtenerIp() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String ip = request.getHeader("X-Forwarded-For");
                if (ip == null || ip.isBlank()) {
                    ip = request.getRemoteAddr();
                }
                return ip != null ? ip : "unknown";
            }
        } catch (Exception e) {
            log.warn("No se pudo obtener la IP del cliente: {}", e.getMessage());
        }
        return "unknown";
    }
}
