package com.trinidad.citas.service;

import com.trinidad.citas.config.JwtService;
import com.trinidad.citas.dto.LoginRequest;
import com.trinidad.citas.dto.LoginResponse;
import com.trinidad.citas.model.Usuario;
import com.trinidad.citas.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final IntentoLoginService intentoLoginService;

    public LoginResponse login(LoginRequest request) {
        String ip = obtenerIp();
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername()).orElse(null);

        if (usuario != null && usuario.isBloqueado()) {
            intentoLoginService.registrarIntento(request.getUsername(), false, ip, "Cuenta bloqueada");
            throw new LockedException("Cuenta bloqueada por múltiples intentos fallidos");
        }

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception e) {
            if (usuario != null) {
                usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);
                if (intentoLoginService.debeBloquear(request.getUsername())) {
                    usuario.setBloqueado(1);
                }
                usuarioRepository.save(usuario);
            }
            intentoLoginService.registrarIntento(request.getUsername(), false, ip,
                e instanceof BadCredentialsException ? "Credenciales inválidas" : e.getMessage());
            throw e;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails);

        usuario = usuarioRepository.findByUsername(request.getUsername()).orElseThrow();
        usuario.setFechaUltLogin(LocalDateTime.now());
        usuario.setIntentosFallidos(0);
        if (usuario.isBloqueado()) {
            usuario.setBloqueado(0);
        }
        usuarioRepository.save(usuario);

        intentoLoginService.registrarIntento(request.getUsername(), true, ip, null);

        return LoginResponse.builder()
            .token(token)
            .tipo("Bearer")
            .username(usuario.getUsername())
            .email(usuario.getEmail())
            .roles(usuario.getRoles().stream().map(r -> r.getNombre()).collect(Collectors.toSet()))
            .expiracionMs(jwtService.getExpirationMs())
            .build();
    }

    private String obtenerIp() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            String ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.isBlank()) {
                ip = request.getRemoteAddr();
            }
            return ip;
        }
        return "unknown";
    }
}
